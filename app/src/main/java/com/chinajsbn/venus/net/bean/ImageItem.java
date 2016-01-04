package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/9/7.
 */
public class ImageItem implements Serializable {
    private String contentId;
    private String contentName;
    private String contentUrl;

    //
    private String desc;
    private String style;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public ImageItem(String contentId, String contentName, String contentUrl, String desc, String style) {

        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
        this.desc = desc;
        this.style = style;
    }
}
