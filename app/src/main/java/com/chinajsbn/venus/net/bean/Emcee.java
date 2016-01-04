package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/10/16.
 * f4 主持人/摄影师 作品
 */
@Table(name = "Emcee")
public class Emcee {

    private int id;

    @Column(column = "costPrice")
    private int    costPrice;

    @Column(column = "description")
    private String description;

    @Column(column = "imageUrl")
    private String imageUrl;

    @Column(column = "shootingAdress")
    private String shootingAdress;

    @Column(column = "shootingTime")
    private String shootingTime;

    @Column(column = "videoUrl")
    private String videoUrl;

    @Column(column = "videoWorkName")
    private String videoWorkName;

    @Column(column = "tag")
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
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

    public String getShootingAdress() {
        return shootingAdress;
    }

    public void setShootingAdress(String shootingAdress) {
        this.shootingAdress = shootingAdress;
    }

    public String getShootingTime() {
        return shootingTime;
    }

    public void setShootingTime(String shootingTime) {
        this.shootingTime = shootingTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoWorkName() {
        return videoWorkName;
    }

    public void setVideoWorkName(String videoWorkName) {
        this.videoWorkName = videoWorkName;
    }

    public Emcee(){}

    public Emcee(int costPrice, String description, String imageUrl, String shootingAdress, String shootingTime, String videoUrl, String videoWorkName) {

        this.costPrice = costPrice;
        this.description = description;
        this.imageUrl = imageUrl;
        this.shootingAdress = shootingAdress;
        this.shootingTime = shootingTime;
        this.videoUrl = videoUrl;
        this.videoWorkName = videoWorkName;
    }
}
