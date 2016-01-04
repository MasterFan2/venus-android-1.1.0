package com.chinajsbn.venus.net.bean;

/**
 * 样片详情
 *
 * Created by MasterFan on 2015/6/29.
 * description:
 */
public class SimpleDetail {

    private String contentDescription;
    private String contentId;
    private String contentName;
    private String contentUrl;//图片地址
    private String createtime;

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public SimpleDetail(String contentDescription, String contentId, String contentName, String contentUrl, String createtime) {

        this.contentDescription = contentDescription;
        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
        this.createtime = createtime;
    }
}
