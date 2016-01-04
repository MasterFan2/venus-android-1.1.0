package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * Created by master on 15-7-30.
 */
public class DetailSubImg implements Serializable {
    private String imageUrl;
    private int weight;

    public DetailSubImg(String imageUrl, int weight) {
        this.imageUrl = imageUrl;
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
