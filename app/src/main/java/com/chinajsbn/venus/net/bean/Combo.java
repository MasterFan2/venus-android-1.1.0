package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * dish list item
 * Created by 13510 on 2015/8/31.
 */
public class Combo implements Serializable {
    private String aliasName;
    private String[] mealPackDishList;
    private String mealPackId;
    private String mealPackName;
    private String mealPackPrice;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String[] getMealPackDishList() {
        return mealPackDishList;
    }

    public void setMealPackDishList(String[] mealPackDishList) {
        this.mealPackDishList = mealPackDishList;
    }

    public String getMealPackId() {
        return mealPackId;
    }

    public void setMealPackId(String mealPackId) {
        this.mealPackId = mealPackId;
    }

    public String getMealPackName() {
        return mealPackName;
    }

    public void setMealPackName(String mealPackName) {
        this.mealPackName = mealPackName;
    }

    public String getMealPackPrice() {
        return mealPackPrice;
    }

    public void setMealPackPrice(String mealPackPrice) {
        this.mealPackPrice = mealPackPrice;
    }

    public Combo(String aliasName, String[] mealPackDishList, String mealPackId, String mealPackName, String mealPackPrice) {

        this.aliasName = aliasName;
        this.mealPackDishList = mealPackDishList;
        this.mealPackId = mealPackId;
        this.mealPackName = mealPackName;
        this.mealPackPrice = mealPackPrice;
    }
}
