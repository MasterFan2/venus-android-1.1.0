package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/9/7.
 */
public class SchemeStyle implements Serializable {

    private int isUsed;
    private int styleId;
    private String styleName;
    private int weight;

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public SchemeStyle(int isUsed, int styleId, String styleName, int weight) {

        this.isUsed = isUsed;
        this.styleId = styleId;
        this.styleName = styleName;
        this.weight = weight;
    }
}
