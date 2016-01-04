package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by MasterFan on 2015/7/2.
 * description:
 */
@Table(name = "Style")
public class Style {

    @Id
    @Column(column = "styleId")
    private String styleId;

    @Column(column = "styleName")
    private String styleName;

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Style() {

    }

    public Style(String styleId, String styleName) {

        this.styleId = styleId;
        this.styleName = styleName;
    }
}
