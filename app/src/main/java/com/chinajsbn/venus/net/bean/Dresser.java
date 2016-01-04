package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/10/16.
 * f4化妆师/摄影师 作品
 */
@Table(name = "Dresser")
public class Dresser implements Serializable{

    private String[] detailImages;

    private int id;

    @Column(column = "imageUrl")
    private String imageUrl;

    @Column(column = "parentName")
    private String parentName;

    @Column(column = "cacheImgs")
    private String cacheImgs;

    @Column(column = "tag")
    private String tag;//cache 用，

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCacheImgs() {
        return cacheImgs;
    }

    public void setCacheImgs(String cacheImgs) {
        this.cacheImgs = cacheImgs;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String[] detailImages) {
        this.detailImages = detailImages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Dresser(){}

    public Dresser(String[] detailImages, String imageUrl) {

        this.detailImages = detailImages;
        this.imageUrl = imageUrl;
    }
}
