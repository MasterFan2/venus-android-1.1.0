package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 摄影师/造型师 作品
 * Created by MasterFan on 2015/6/18.
 * description:
 */
@Table(name = "PhotographerWorks")
public class PhotographerWorks implements Serializable{

    @Id
    @Column(column = "contentId")
    private String contentId;

    @Column(column = "contentName")
    private String contentName;

    @Column(column = "contentUrl")
    private String contentUrl;

    @Column(column = "detailUrl")
    private String detailUrl;

    @Column(column = "weight")
    private String weight;

    @Column(column = "pName")
    private String pName;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
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

    public PhotographerWorks() {}

    public PhotographerWorks(String contentId, String contentName, String contentUrl, String detailUrl, String weight) {

        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
        this.detailUrl = detailUrl;
        this.weight = weight;
    }
}
