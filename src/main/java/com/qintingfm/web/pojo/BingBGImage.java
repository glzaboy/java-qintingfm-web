package com.qintingfm.web.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Bing 背景图
 */
public class BingBGImage {

    public static class Image{
        private String url;
        @JsonProperty("copyright")
        private String desc;
        @JsonProperty("copyrightlink")
        private String link;

        final private String bingHost="https://cn.bing.com/";


        public Image() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImageUrl(){
            if(getUrl()!=null && (getUrl().startsWith("http://") || getUrl().startsWith("https://"))){
                return getUrl();
            }
            return new StringBuilder(bingHost).append(this.getUrl()).toString();
        }
    }
    @JsonProperty("images")
    private List<Image> imageList;


    public BingBGImage() {
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
