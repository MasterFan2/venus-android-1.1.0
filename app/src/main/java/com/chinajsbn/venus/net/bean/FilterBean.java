package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * Created by 13510 on 2015/10/14.
 */
@Table(name = "FilterBean")
public class FilterBean {

    @Column(column = "id")
    private int id;

    @Id
    @Column(column = "name")
    private String name;

    @Transient
    private boolean checked;

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public FilterBean(){}

    public FilterBean(String name, boolean checked) {
        this.checked = checked;
        this.name = name;
    }

    public FilterBean(boolean checked, int id, String name) {

        this.checked = checked;
        this.id = id;
        this.name = name;
    }
}
