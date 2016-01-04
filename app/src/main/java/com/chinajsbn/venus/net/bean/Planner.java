package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13510 on 2015/10/16.
 */
@Table(name = "Planner")
public class Planner implements Serializable{

    private int id;

    @Column(column = "description")
    private String   description;

    private List<Dresser> list;

    @Column(column = "photoUrl")
    private String    photoUrl;

    @Column(column = "plannerId")
    private int       plannerId;

    @Column(column = "plannerName")
    private String    plannerName;

    @Column(column = "price")
    private int       price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Dresser> getList() {
        return list;
    }

    public void setList(List<Dresser> list) {
        this.list = list;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(int plannerId) {
        this.plannerId = plannerId;
    }

    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String plannerName) {
        this.plannerName = plannerName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Planner(){}

    public Planner(String description, ArrayList<Dresser> list, String photoUrl, int plannerId, String plannerName, int price) {

        this.description = description;
        this.list = list;
        this.photoUrl = photoUrl;
        this.plannerId = plannerId;
        this.plannerName = plannerName;
        this.price = price;
    }
}
