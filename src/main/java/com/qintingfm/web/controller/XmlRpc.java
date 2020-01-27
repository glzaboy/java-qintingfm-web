package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.CategoryJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.service.AppUserDetailsService;
import com.qintingfm.web.service.XmlRpcServer;
import com.qintingfm.web.service.xmlrpcconfig.RpcController;
import com.qintingfm.web.service.xmlrpcconfig.StreamConfig;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/xmlrpc")
@Slf4j
@Transactional
public class XmlRpc {
    @Autowired
    AppUserDetailsService appUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CategoryJpa categoryJpa;
    @Autowired
    BlogJpa blogJpa;
    @Autowired
    Manager manager;
    @RequestMapping(value = {"/xmlrpcserver",".php"} ,method = {RequestMethod.POST,RequestMethod.OPTIONS},produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer(@Autowired HttpServletRequest  request, @Autowired HttpServletResponse response) throws IOException, XmlRpcException {
        ServletInputStream inputStream = request.getInputStream();
        response.setContentType("application/xml;charset=utf-8");
        XmlRpcServer xmlRpcServer=new XmlRpcServer();
        xmlRpcServer.setRpcController(new RpcController());
        xmlRpcServer.setStreamConfig(new StreamConfig());
        try {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            byte[] bytes=new byte[1024];
            int readnum =0;
            do {
                 readnum = inputStream.read(bytes, 0, 10);
                 if(readnum>0){
                     byteArrayOutputStream.write(bytes,0,readnum);
                 }

            }while (readnum>0);
            log.info(byteArrayOutputStream.toString());
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            XmlRpcRequestParser xmlRequestParser = xmlRpcServer.getXmlRequestParser(byteInputStream);
            String username=xmlRequestParser.getParams().get(1).toString();
            String password=xmlRequestParser.getParams().get(2).toString();
            if(xmlRequestParser.getMethodName()!=null && xmlRequestParser.getMethodName().equals("blogger.deletePost")){
                username=xmlRequestParser.getParams().get(2).toString();
                password=xmlRequestParser.getParams().get(3).toString();
            }
            if(!login(username,password)){
                xmlRpcServer.ResponseError(response.getOutputStream(),1001,"登录出错");
                return "";
            }
            log.info(xmlRequestParser.getMethodName().toString());
            switch (xmlRequestParser.getMethodName()){
                case "blogger.getUsersBlogs":
                    xmlRpcServer.Response(response.getOutputStream(),getUserBlog());
                    break;
                case "metaWeblog.getCategories":
                    xmlRpcServer.Response(response.getOutputStream(),getCaterory());
                    break;
                case "metaWeblog.getRecentPosts":
                    xmlRpcServer.Response(response.getOutputStream(),getRecentPosts(xmlRequestParser));
                    break;
                case "metaWeblog.newPost":
                    xmlRpcServer.Response(response.getOutputStream(),newPost(xmlRequestParser));
                    break;
                case "metaWeblog.editPost":
                    xmlRpcServer.Response(response.getOutputStream(),editPost(xmlRequestParser));
                    break;
                case "metaWeblog.getPost":
                    xmlRpcServer.Response(response.getOutputStream(),getPost(xmlRequestParser));
                    break;
                case "blogger.deletePost":
                    xmlRpcServer.Response(response.getOutputStream(),deletePost(xmlRequestParser));
                    break;
                case "metaWeblog.newMediaObject":
                    xmlRpcServer.Response(response.getOutputStream(),newMediaObject(xmlRequestParser));
                    break;
            }
        } catch (XmlRpcException e) {
            try {
                xmlRpcServer.ResponseError(response.getOutputStream(),e.code,e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        } catch (SAXException e) {
            try {
                xmlRpcServer.ResponseError(response.getOutputStream(),1001,e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        } catch (ManagerException e) {
            try {
                xmlRpcServer.ResponseError(response.getOutputStream(),1001,e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        }
        return "";
    }
    private Map<String,String> newMediaObject(XmlRpcRequestParser xmlRequestParser) throws ManagerException{
        @SuppressWarnings("unchecked")
        Map<String, Object> stringObjectMap = (Map<String, Object>) xmlRequestParser.getParams().get(3);
        Map<String,String> mediaObject=new HashMap<>();
        byte[] bits = (byte[])stringObjectMap.get("bits");
        String name = (String)stringObjectMap.get("name");
        String s = DigestUtils.md5DigestAsHex(bits);
        StorageObject put =  manager.put(bits, s);
        mediaObject.put("url",put.getUrl());
        return mediaObject;
    }


    private Boolean deletePost(XmlRpcRequestParser xmlRequestParser) {
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(1).toString());
        Optional<Blog> byId = blogJpa.findById(postId);
        if(byId.isPresent()){
            blogJpa.delete(byId.get());
        }
        return true;
    }

    private Map<String,Object> getPost(XmlRpcRequestParser xmlRequestParser) {
        Map<String,Object> postMap=new HashMap<>();
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(0).toString());
        Optional<Blog> byId = blogJpa.findById(postId);
        if(byId.isPresent()){
            Blog blog = byId.get();
            postMap.put("dateCreated",blog.getDateCreated()==null?new Date():blog.getDateCreated());
            postMap.put("title",blog.getTitle());
            postMap.put("postid",blog.getPostId());
            BlogCont blogCont = blog.getBlogCont();
            if(blogCont!=null){
                postMap.put("description",blogCont.getCont());
            }else{
                postMap.put("description","");
            }
            postMap.put("link",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString().replaceAll("^/xmlrpc\\.*","/")+"/blog/view/"+blog.getPostId());
        }
        return postMap;
    }

    Vector<Map<String,Object>> getRecentPosts(XmlRpcRequestParser xmlRequestParser) {
        Vector<Map<String,Object>> mapVector=new Vector<>();
        Integer integer = Integer.valueOf(xmlRequestParser.getParams().get(3).toString());
        if (integer>=100){
            integer=100;
        }
        if (integer<=100){
            integer=50;
        }
        Page<Blog> post_id_desc = blogJpa.findAll(PageRequest.of(0,integer,Sort.by(new Sort.Order(Sort.Direction.DESC,"postId"))));
        post_id_desc.get().forEach(item->{
            Map<String,Object>post=new HashMap<>();
            post.put("dateCreated",item.getDateCreated()==null?new Date():item.getDateCreated());
            post.put("title",item.getTitle());
            post.put("postid",item.getPostId());
            BlogCont blogCont = item.getBlogCont();
            if(blogCont!=null){
                post.put("description",blogCont.getCont());
            }else{
                post.put("description","");
            }
            post.put("link",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString().replaceAll("^/xmlrpc\\.*","/")+"/blog/view/"+item.getPostId());
            mapVector.add(post);
        });
        return mapVector;

    }
    private String editPost(XmlRpcRequestParser xmlRequestParser) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        Optional<Blog> byId = blogJpa.findById(Integer.valueOf(xmlRequestParser.getParams().get(0).toString()));
        if(!byId.isPresent()){
            Blog blog=new Blog();
            blog.setTitle(stringObjectHashMap.get("title").toString());
            BlogCont blogCont=new BlogCont();
            blogCont.setCont(stringObjectHashMap.get("description").toString());
            blog.setBlogCont(blogCont);
            if(stringObjectHashMap.get("dateCreated")!=null){
                Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
                blog.setDateCreated(dateCreated);
            }else{
                blog.setDateCreated(new Date());
            }
            blogJpa.save(blog);
        }else {
            Blog blog=byId.get();
            blog.setTitle(stringObjectHashMap.get("title").toString());
            blog.getBlogCont().setCont(stringObjectHashMap.get("description").toString());
            if(stringObjectHashMap.get("dateCreated")!=null){
                Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
                blog.setDateCreated(dateCreated);
            }else{
                blog.setDateCreated(new Date());
            }
            blogJpa.save(blog);
        }
        return "";
    }
    private String newPost(XmlRpcRequestParser xmlRequestParser) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        Blog blog=new Blog();
        blog.setTitle(stringObjectHashMap.get("title").toString());
        BlogCont blogCont=new BlogCont();
        blogCont.setCont(stringObjectHashMap.get("description").toString());
        blog.setBlogCont(blogCont);
        if(stringObjectHashMap.get("dateCreated")!=null){
            Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
            blog.setDateCreated(dateCreated);
        }else{
            blog.setDateCreated(new Date());
        }
        blogJpa.save(blog);
        return blog.getPostId().toString();
    }

    private Vector<Map<String,String>> getCaterory() {
        Vector<Map<String,String>> mapVector=new Vector<>();

        List<Category> all = categoryJpa.findAll();
        all.forEach((item)->{
            Map<String,String> caterories=new HashMap<>();
            caterories.put("title",item.getTitle());
            caterories.put("categoryid",item.getCatId().toString());
            caterories.put("description",item.getDescription());
            caterories.put("rssUrl","");
            caterories.put("htmlUrl","");
            mapVector.add(caterories);
        });

        return mapVector;
    }
    private Vector<Map<String,String>> getUserBlog(){
        Vector<Map<String,String>> mapVector=new Vector<>();
        Map<String,String>userBlog=new HashMap<>();
        userBlog.put("blogid","1");
        userBlog.put("blogName","qintingfm");
        userBlog.put("url",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("xmlrpc",ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("isAdmin","1");
        mapVector.add(userBlog);
        return mapVector;
    }
    @RequestMapping(value = {"/xmlrpcserver",".php"} ,method = {RequestMethod.GET},produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer( ) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<rsd version=\"1.0\" xmlns=\"http://archipelago.phrasewise.com/rsd\">\n" +
                "\t<service>\n" +
                "\t\t<engineName>qintingfm</engineName>\n" +
                "\t\t<engineLink>http://qintingfm.com/</engineLink>\n" +
                "\t\t<homePageLink>http://qintingfm.com/</homePageLink>\n" +
                "\t\t<apis>\n" +
                "\t\t\t<api name=\"MetaWeblog\" blogID=\"1\" preferred=\"false\" apiLink=\""+ ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()+"\" />\n" +
                "\t\t</apis>\n" +
                "\t</service>\n" +
                "</rsd>";
    }
    boolean login(String username,String password){
        try{
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
            if(userDetails==null){
                return false;
            }
            boolean matches = passwordEncoder.matches( password,userDetails.getPassword());
            if(matches){
                return true;
            }
        }catch(UsernameNotFoundException exception){
            return false;
        }
        return false;
    }

}
