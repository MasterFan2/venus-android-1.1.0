package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/9/29.
 */
@Table(name = "DoublePhotographer")
public class DoublePhotographer {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(column = "tag")
    private String tag;

    @Foreign(column = "photographer1", foreign = "personId")
    private Photographer photographer1;

    @Foreign(column = "photographer2", foreign = "personId")
    private Photographer photographer2;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Photographer getPhotographer1() {
        return photographer1;
    }

    public void setPhotographer1(Photographer photographer1) {
        this.photographer1 = photographer1;
    }

    public Photographer getPhotographer2() {
        return photographer2;
    }

    public void setPhotographer2(Photographer photographer2) {
        this.photographer2 = photographer2;
    }

    public DoublePhotographer(){}

    public DoublePhotographer(String tag, Photographer photographer1, Photographer photographer2) {

        this.tag = tag;
        this.photographer1 = photographer1;
        this.photographer2 = photographer2;
    }
}
