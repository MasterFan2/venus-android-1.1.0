package com.chinajsbn.venus.net.bean;

/**
 * 婚纱
 * 、
 * Created by MasterFan on 2015/7/6.
 * description:
 */
public class WeddingDress {

    private String contentId;
    private String contentName;
    private String contentUrl;
    private String detailUrl;
    private String weight;

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

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public WeddingDress() {

    }

    public WeddingDress(String contentId, String contentName, String contentUrl, String detailUrl, String weight) {

        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
        this.detailUrl = detailUrl;
        this.weight = weight;
    }
}
