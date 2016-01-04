package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/6/24.
 * description:
 */
public class Submenu {

    private String contentId;
    private String moduleName;

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

    public Submenu(String contentId, String moduleName) {

        this.contentId = contentId;
        this.moduleName = moduleName;
    }
}
