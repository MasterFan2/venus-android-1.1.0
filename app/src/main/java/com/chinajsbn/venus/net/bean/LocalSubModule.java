package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/10/26.
 */
@Table(name = "LocalSubModule")
public class LocalSubModule {

    @Id
    @Column(column = "contentId")
    private String contentId;

    @Column(column = "moduleName")
    private String moduleName;

    @Column(column = "parentId")
    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public LocalSubModule() {}

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

    public LocalSubModule(String contentId, String moduleName, String parentId) {
        this.contentId = contentId;
        this.moduleName = moduleName;
        this.parentId = parentId;
    }
}
