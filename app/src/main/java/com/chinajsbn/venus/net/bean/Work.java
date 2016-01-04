package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/12/2.
 */
public class Work implements Serializable {

    private String actorNameFemale;
    private String actorNameMale;
    private String advertType;
    private String contentId;
    private String contentName;
    private String contentUrl;

    public String getActorNameFemale() {
        return actorNameFemale;
    }

    public void setActorNameFemale(String actorNameFemale) {
        this.actorNameFemale = actorNameFemale;
    }

    public String getActorNameMale() {
        return actorNameMale;
    }

    public void setActorNameMale(String actorNameMale) {
        this.actorNameMale = actorNameMale;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
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

    public Work(String actorNameFemale, String actorNameMale, String advertType, String contentId, String contentName, String contentUrl) {

        this.actorNameFemale = actorNameFemale;
        this.actorNameMale = actorNameMale;
        this.advertType = advertType;
        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
    }
}
