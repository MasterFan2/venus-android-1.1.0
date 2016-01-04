package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/10/26.
 */
@Table(name = "Cache")
public class Cache {

    @Id
    @Column(column = "pageName")
    private String pageName;//页面名称

    @Column(column = "date")
    private String date;    //日期

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Cache(){}

    public Cache(String pageName, String date) {

        this.pageName = pageName;
        this.date = date;
    }
}
