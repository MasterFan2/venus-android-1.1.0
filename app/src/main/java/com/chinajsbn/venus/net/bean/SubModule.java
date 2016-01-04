package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MasterFan on 2015/6/24.
 * description:
 */
@Table(name = "SubModule")
public class SubModule implements Serializable {

    @Column(column = "blockCode")
    private String blockCode;

    @Id
    @Column(column = "contentId")
    private String contentId;

    private ArrayList<MenuLink> menuLink;

    @Column(column = "moduleName")
    private String moduleName;

    private ArrayList<SubModule> subModule;

    @Column(column = "type")
    private boolean type;

    @Column(column = "iadvert")
    private int iadvert;

    public SubModule() {
    }

    public int getIadvert() {
        return iadvert;
    }

    public void setIadvert(int iadvert) {
        this.iadvert = iadvert;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ArrayList<MenuLink> getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(ArrayList<MenuLink> menuLink) {
        this.menuLink = menuLink;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ArrayList<SubModule> getSubModule() {
        return subModule;
    }

    public void setSubModule(ArrayList<SubModule> subModule) {
        this.subModule = subModule;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public SubModule(String blockCode, String contentId, ArrayList<MenuLink> menuLink, String moduleName, ArrayList<SubModule> subModule, boolean type) {

        this.blockCode = blockCode;
        this.contentId = contentId;
        this.menuLink = menuLink;
        this.moduleName = moduleName;
        this.subModule = subModule;
        this.type = type;
    }
}
