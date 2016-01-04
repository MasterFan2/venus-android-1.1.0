package com.chinajsbn.venus.net.bean;

/**
 * 婚纱样式
 * 、
 * Created by MasterFan on 2015/7/6.
 * description:
 */
public class WeddingDressStyle {

    private int styleId;
    private String styleName;

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

    public WeddingDressStyle() {

    }

    public WeddingDressStyle(int styleId, String styleName) {

        this.styleId = styleId;
        this.styleName = styleName;
    }
}
