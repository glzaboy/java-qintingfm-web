package com.qintingfm.web.common;

import java.util.Map;

/**
 * 网页版本ajax请求
 */
public class AjaxDto {
    public String message;
    public String link;

    public String autoHide;

    public int autoJump;

    public Map<String,String> error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAutoHide() {
        return autoHide;
    }

    public void setAutoHide(String autoHide) {
        this.autoHide = autoHide;
    }

    public int getAutoJump() {
        return autoJump;
    }

    public void setAutoJump(int autoJump) {
        this.autoJump = autoJump;
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }
}
