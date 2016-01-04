package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * 婚纱
 * Created by 13510 on 2015/11/30.
 */
public class Dress implements Serializable {
    private String description;
    private String imageUrl;
    private String weddingDressName;
    private String weddingDressNo;

    public Dress() {
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWeddingDressName() {
        return weddingDressName;
    }

    public void setWeddingDressName(String weddingDressName) {
        this.weddingDressName = weddingDressName;
    }

    public String getWeddingDressNo() {
        return weddingDressNo;
    }

    public void setWeddingDressNo(String weddingDressNo) {
        this.weddingDressNo = weddingDressNo;
    }

    public Dress(String description, String imageUrl, String weddingDressName, String weddingDressNo) {

        this.description = description;
        this.imageUrl = imageUrl;
        this.weddingDressName = weddingDressName;
        this.weddingDressNo = weddingDressNo;
    }
}
