package com.chinajsbn.venus.net.bean;

/**
 * 摄影师风格
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
public class StyleResp {
    private String styleName;
    private int styleId;

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public StyleResp(String styleName, int styleId) {

        this.styleName = styleName;
        this.styleId = styleId;
    }
}
