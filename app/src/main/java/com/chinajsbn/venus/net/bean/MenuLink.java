package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by MasterFan on 2015/6/24.
 * description:
 */
@Table(name = "MenuLink")
public class MenuLink implements Serializable {

    private int id;

    @Column(column = "menuId")
    private String menuId;

    @Column(column = "menuLinkName")
    private String menuLinkName;

    @Column(column = "menuLinkUrl")
    private String menuLinkUrl;

    public MenuLink() {
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuLinkName() {
        return menuLinkName;
    }

    public void setMenuLinkName(String menuLinkName) {
        this.menuLinkName = menuLinkName;
    }

    public String getMenuLinkUrl() {
        return menuLinkUrl;
    }

    public void setMenuLinkUrl(String menuLinkUrl) {
        this.menuLinkUrl = menuLinkUrl;
    }

    public MenuLink(String menuId, String menuLinkName, String menuLinkUrl) {

        this.menuId = menuId;
        this.menuLinkName = menuLinkName;
        this.menuLinkUrl = menuLinkUrl;
    }
}
