package com.qintingfm.web.util;

/**
 * HTML帮助类
 * @author guliuzhong
 */
public class HtmlUtil {
    /**
     * 去除html代码中含有的标签
     * @param htmlStr
     * @return
     */
    public static String delHtmlTags(String htmlStr) {
        //定义script的正则表达式，去除js可以防止注入
        String scriptRegex="<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex="<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex="<[^>]+>";
        //定义空格,回车,换行符,制表符
        String spaceRegex = "\\s*|\t|\r|\n";
        String naRegex = "\t|\r|\n";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(naRegex, "");
        htmlStr = htmlStr.replaceAll("\\s{1,} ", " ");
        return htmlStr.trim();
    }
    public static String decodeEntityHtml(String html){
        html=html.replaceAll("&ldquo;","“");
        html=html.replaceAll("&rdquo;","”");
        html=html.replaceAll("&amp;","&");
        return html;
    }
}
