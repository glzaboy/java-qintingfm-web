package com.qintingfm.web.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Bing 背景图
 *
 * @author guliuzhong
 */
@JsonIgnoreProperties({"startdate", "fullstartdate", "enddate"})
public class BingBgImage {

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

    public static class Image {
        public static final String HTTP = "http://";
        public static final String HTTPS = "https://";
        public static final String BINGHOST = "https://cn.bing.com/";
        private String url;
        @JsonProperty("copyright")
        private String desc;
        @JsonProperty("copyrightlink")
        private String link;

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

        public String getImageUrl() {
            if (url == null) {
                return null;
            }
            if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
                return url;
            }
            return BINGHOST + url;
        }
    }
}
