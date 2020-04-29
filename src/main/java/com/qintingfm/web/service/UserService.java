package com.qintingfm.web.service;

import com.qintingfm.web.common.exception.Business;
import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.controller.UserController;
import com.qintingfm.web.jpa.UserJpa;
import com.qintingfm.web.jpa.UserRegisterJpa;
import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.jpa.entity.User;
import com.qintingfm.web.jpa.entity.UserRegister;
import com.qintingfm.web.pojo.request.UserRegisterPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
@Service
@Slf4j
public class UserService extends BaseService {
    private UserJpa userJpa;
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String mailUser;
    UserRegisterJpa userRegisterJpa;

    SettingService settingService;

    HtmlService htmlService;

    PasswordEncoder passwordEncoder;

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @Autowired
    public void setUserJpa(UserJpa userJpa) {
        this.userJpa = userJpa;
    }


    @Autowired
    public void setUserRegisterJpa(UserRegisterJpa userRegisterJpa) {
        this.userRegisterJpa = userRegisterJpa;
    }

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }
    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(String userName) {
        if (userName == null || userName.isEmpty()) {
            return null;
        }
        Optional<User> one = userJpa.findByUsername(userName);
        return one.orElse(null);
    }

    public User getUser(Long userId) {
        if (userId == null || userId == 0) {
            return null;
        }
        Optional<User> one = userJpa.findById(userId);
        return one.orElse(null);
    }

    public Optional<User> validPassword(String userName,String password){
        User user = getUser(userName);
        if (user == null) {
            return Optional.empty();
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (matches) {
            User user1=new User();
            BeanUtils.copyProperties(user,user1,"password");
            return Optional.of(user1);
        }
        return Optional.empty();
    }
    /**
     * 注册用户
     *
     * @param userRegisterPojo 用户注册参数
     * @return 用户注册信息
     */
    @Transactional(rollbackFor = {BusinessException.class})
    public UserRegister register(UserRegisterPojo userRegisterPojo) {
        this.validatePojoAndThrow(userRegisterPojo);
        UserRegister userRegisterExam = new UserRegister();
        userRegisterExam.setUserName(userRegisterPojo.getUserName());
        long count = userRegisterJpa.count(Example.of(userRegisterExam));
        if (count > 0) {
            Business.BusinessBuilder builder = Business.builder();
            Set<Business> businessSet = new HashSet<>();
            builder.field("username").message("用户已经被占用，不能注册");
            businessSet.add(builder.build());

            buildAndThrowBusinessException(userRegisterPojo.getClass(), businessSet);
        }


        UserRegister userRegister = new UserRegister();
        BeanUtils.copyProperties(userRegisterPojo, userRegister);
        userRegister.setCreateDate(new Date());
        userRegister.setIsActive(false);
        userRegister.setActiveKey(UUID.randomUUID());
        UserRegister save = userRegisterJpa.saveAndFlush(userRegister);

        if (save.getRegisterId() != null) {
            Stream<SettingItem> register = settingService.getSettings("register");
            Map<String, String> collect = register.collect(Collectors.toMap(SettingItem::getKey, SettingItem::getValue));
            if (settingService.isEnable(collect)) {
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("mail/mail");
                String activeUrl = "";
                try {
                    Method detail = UserController.class.getDeclaredMethod("active", ModelAndView.class, String.class);
                    Map<String, String> buildAgr = new HashMap<>(2);
                    buildAgr.put("activeKey", save.getActiveKey().toString());
                    activeUrl = MvcUriComponentsBuilder.fromMethod(UserController.class, detail, null, save.getActiveKey().toString()).encode().toUriString();
                } catch (NoSuchMethodException e) {
                    log.error("用户注册激活地址生成出错没有找到激活地址");
                }
                String mailText = "您好您已经成功注册我们的网站请点击下面的地址进行激活。<a href=\"" + activeUrl + "\">" + activeUrl + "</a>";

                modelAndView.addObject("mail_text", mailText);

                String html = htmlService.renderModelAndViewToString(modelAndView);
                String text = htmlService.filterSimpleText(html);

                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper;
                try {
                    helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(mailUser);
                    helper.setText(text, html);
                    helper.setTo(save.getEmail());
                    helper.setSubject("您的帐号需要激活");
                } catch (MessagingException e) {
                    log.error("注册用户发送邮件失败。");
                }
                mailSender.send(mimeMessage);
            }
        }

        return save;
    }
    @Transactional(rollbackFor = {BusinessException.class})
    public User active(String activeKey){
        UserRegister userRegisterExam = new UserRegister();
        userRegisterExam.setActiveKey(UUID.fromString(activeKey));
        Optional<UserRegister> one = userRegisterJpa.findOne(Example.of(userRegisterExam));
        one.orElseThrow(()->{return new BusinessException("激活 key 不存在",null);});
        UserRegister userRegister = one.get();
        if(userRegister.getIsActive()){
            throw new BusinessException("帐号已经被激活，如果还有问题请联系管理员",null);
        }
        userRegister.setIsActive(true);
        userRegister.setActiveDate(new Date());
        userRegisterJpa.save(userRegister);
        User user=new User();
        user.setUsername(userRegister.getUserName());
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        String password = generalPassword(8, true);
        user.setPassword(passwordEncoder.encode(password));
        User save = userJpa.save(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mail/mail");
        String loginUrl = null;
        try {
            Method detail = UserController.class.getDeclaredMethod("loginPage", ModelAndView.class);
            Object[] args={null};
            loginUrl = MvcUriComponentsBuilder.fromMethod(UserController.class, detail,args).encode().toUriString();
        } catch (NoSuchMethodException e) {
            log.error("用户注册激活地址生成出错没有找到激活地址");
        }
        String mailText = "您好您的帐号已经激活，您的密码为 "+password+"。请立即进行登录并修改密码。登录地址<a href=\"" + loginUrl + "\">" + loginUrl + "</a>";
        modelAndView.addObject("mail_text", mailText);

        String html = htmlService.renderModelAndViewToString(modelAndView);
        String text = htmlService.filterSimpleText(html);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailUser);
            helper.setText(text, html);
            helper.setTo(userRegister.getEmail());
            helper.setSubject("您的帐号成功激活");
        } catch (MessagingException e) {
            log.error("激活用户发送邮件失败。");
        }
        mailSender.send(mimeMessage);
        return save;
    }

    /**
     * 生成用户密码
     * @param len
     * @param readAble
     * @return
     */
    public String generalPassword(int len,boolean readAble){
        StringBuilder stringTab=new StringBuilder() ;
        if(readAble){
            stringTab.append("BCEFGHJKMPQRTVWXY2346789");
        }else{
            stringTab.append("ABCDEFGHJKMNPQRSTUVWXY0123456789abcdefghjkmnrstuvwxyz");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            double random = Math.random()*10000;
            int offset = ((int)random) % stringTab.length();
            stringBuilder.append(stringTab.substring(offset, offset + 1));
        }
        return stringBuilder.toString();
    }

}
