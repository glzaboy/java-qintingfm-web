package com.qintingfm.web.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Bing 背景图
 * @author guliuzhong
 */
@JsonIgnoreProperties({"startdate","fullstartdate","enddate"})
public class BingBgImage {

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
            if(url==null){
                return null;
            }
            if(url.startsWith("http://") || url.startsWith("https://")){
                return url;
            }
            return bingHost+url;
        }
    }
    @JsonProperty("images")
    private List<Image> imageList;


    public BingBgImage() {
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
