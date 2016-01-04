package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/11/9.
 */
@Table(name = "MoreTag")
public class MoreTag {

    @Id
    @Column(column = "name")
    private String name;

    @Column(column = "valu")
    private String valu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValu() {
        return valu;
    }

    public void setValu(String valu) {
        this.valu = valu;
    }

    public MoreTag(String name, String valu) {

        this.name = name;
        this.valu = valu;
    }

    public MoreTag() {

    }
}
