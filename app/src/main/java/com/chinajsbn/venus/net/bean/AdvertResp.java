package com.chinajsbn.venus.net.bean;

/**
 * 广告列表数据
 *
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class AdvertResp {

    private String contentId;
    private String contentName;
    private String contentUrl;

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

    public AdvertResp(String contentId, String contentName, String contentUrl) {

        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
    }
}
