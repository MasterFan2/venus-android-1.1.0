package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MasterFan on 2015/6/18.
 * description:
 */
@Table(name = "HomeMenu")
public class HomeMenu implements Serializable{

    private int id;

    @Column(column = "contentId")
    private String contentId;

    @Column(column = "moduleName")
    private String moduleName;

    private ArrayList<MenuLink> menuLink;

    private ArrayList<SubModule> subModule;

    public HomeMenu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<MenuLink> getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(ArrayList<MenuLink> menuLink) {
        this.menuLink = menuLink;
    }

    public ArrayList<SubModule> getSubModule() {
        return subModule;
    }

    public void setSubModule(ArrayList<SubModule> subModule) {
        this.subModule = subModule;
    }

    public HomeMenu(int id, String contentId, String moduleName, ArrayList<MenuLink> menuLink, ArrayList<SubModule> subModule) {

        this.id = id;
        this.contentId = contentId;
        this.moduleName = moduleName;
        this.menuLink = menuLink;
        this.subModule = subModule;
    }
}
