package com.chinajsbn.venus.net.bean;

import android.support.annotation.ColorInt;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/8/29.
 */
@Table(name = "SimpleStyles")
public class SimpleStyles implements Serializable {

    @Column(column = "description")
    private String description;

    @Column(column = "isUsed")
    private int isUsed;

    @Id
    @Column(column = "shootingStyleId")
    private String shootingStyleId;

    @Column(column = "shootingStyleName")
    private String shootingStyleName;

    @Column(column = "weight")
    private int weight;

    public SimpleStyles(){}

    public SimpleStyles(String description, int isUsed, String shootingStyleId, String shootingStyleName, int weight) {
        this.description = description;
        this.isUsed = isUsed;
        this.shootingStyleId = shootingStyleId;
        this.shootingStyleName = shootingStyleName;
        this.weight = weight;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public String getShootingStyleId() {
        return shootingStyleId;
    }

    public void setShootingStyleId(String shootingStyleId) {
        this.shootingStyleId = shootingStyleId;
    }

    public String getShootingStyleName() {
        return shootingStyleName;
    }

    public void setShootingStyleName(String shootingStyleName) {
        this.shootingStyleName = shootingStyleName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
