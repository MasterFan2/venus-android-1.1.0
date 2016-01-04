package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 品牌类型  / 婚车型号
 * Created by 13510 on 2015/12/16.
 */
@Table(name = "CarType")
public class CarType {

    @Column(column = "id")
    private int id;

    @Id
    @Column(column = "name")
    private String name;

    @Column(column = "desc")
    private String desc;

    @Transient
    private boolean checked;

    @Column(column = "tag")
    private String tag;//supplie & car

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarType(){}

    public CarType(String desc, int id, String name) {

        this.desc = desc;
        this.id = id;
        this.name = name;
    }
}
