package com.qintingfm.web.captcha;


import com.qintingfm.web.captcha.base.AbstractCaptcha;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * png格式验证码
 *
 * @author 王帆
 * @date 2018-07-27 上午 10:08
 */
@Slf4j
public class SpecCaptcha extends AbstractCaptcha {

    public SpecCaptcha() {
    }

    public SpecCaptcha(int width, int height) {
        this();
        setWidth(width);
        setHeight(height);
    }

    public SpecCaptcha(int width, int height, int len) {
        this(width, height);
        setLen(len);
    }

    /**
     * 生成验证码
     *
     * @param out 输出流
     */
    @Override
    public boolean out(OutputStream out) {
        try {
            char[] strs =textChar();
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            /**
             * 填充背景
             */
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            /**
             * 抗锯齿
             */
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            /**
             * 画干扰圆
             */
            drawOval(2, g2d);
            /**
             * 画干扰线
             */
            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            drawBesselLine(1, g2d);
            /**
             * 画字符串
             */
            g2d.setFont(getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fW = width / strs.length;
            int fSp = (fW - (int) fontMetrics.getStringBounds("W", g2d).getWidth()) / 2;
            for (int i = 0; i < strs.length; i++) {
                g2d.setColor(color());
                int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(strs[i]), g2d).getHeight()) >> 1);
                g2d.drawString(String.valueOf(strs[i]), i * fW + fSp + 3, fY - 3);
            }
            g2d.dispose();
            ImageIO.write(bi, "png", out);
            out.flush();
            return true;
        } catch (IOException e) {
            log.error("生成验证码出错，生成图片出错");
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("生成验证码出错，读写文件出错");
            }
        }
        return false;
    }
}