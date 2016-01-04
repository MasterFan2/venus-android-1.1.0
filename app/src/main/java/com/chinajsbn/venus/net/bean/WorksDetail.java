package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * Created by 13510 on 2015/9/23.
 */
public class WorksDetail {
    private String actorNameFemale;
    private String actorNameMale;
    private String contentId;
    private String contentName;
    private String description;
    private ArrayList<WorksDetailImage> detailedImages;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<WorksDetailImage> getDetailedImages() {
        return detailedImages;
    }

    public void setDetailedImages(ArrayList<WorksDetailImage> detailedImages) {
        this.detailedImages = detailedImages;
    }

    public WorksDetail(String actorNameFemale, String actorNameMale, String contentId, String contentName, String description, ArrayList<WorksDetailImage> detailedImages) {

        this.actorNameFemale = actorNameFemale;
        this.actorNameMale = actorNameMale;
        this.contentId = contentId;
        this.contentName = contentName;
        this.description = description;
        this.detailedImages = detailedImages;
    }
}
