package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/11/30.
 * 品牌
 */
@Table(name = "Brands")
public class Brands {

    @Column(column = "description")
    private String description;

    @Column(column = "imageUrl")
    private String imageUrl;

    @Id
    @Column(column = "weddingDressBrandId")
    private int weddingDressBrandId;

    @Column(column = "weddingDressBrandName")
    private String weddingDressBrandName;

    @Column(column = "weight")
    private int weight;

    @Column(column = "tab")
    private int tab;//1              2.              3.

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public Brands() {
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

    public int getWeddingDressBrandId() {
        return weddingDressBrandId;
    }

    public void setWeddingDressBrandId(int weddingDressBrandId) {
        this.weddingDressBrandId = weddingDressBrandId;
    }

    public String getWeddingDressBrandName() {
        return weddingDressBrandName;
    }

    public void setWeddingDressBrandName(String weddingDressBrandName) {
        this.weddingDressBrandName = weddingDressBrandName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Brands(String description, String imageUrl, int weddingDressBrandId, String weddingDressBrandName, int weight) {

        this.description = description;
        this.imageUrl = imageUrl;
        this.weddingDressBrandId = weddingDressBrandId;
        this.weddingDressBrandName = weddingDressBrandName;
        this.weight = weight;
    }
}
