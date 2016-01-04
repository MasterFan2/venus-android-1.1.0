package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/12/14.
 */
@Table(name = "Car")
public class Car {

    @Id
    @Column(column = "weddingCarRentalId")
    private int weddingCarRentalId;

    @Column(column = "isUsed")
    private int isUsed;

    @Column(column = "weight")
    private int weight;

    @Column(column = "coverUrl")
    private String coverUrl;

    @Column(column = "carModelsId")
    private int carModelsId;

    @Column(column = "marketRentalPrice")
    private int marketRentalPrice;

    @Column(column = "carLevelId")
    private int carLevelId;

    @Column(column = "title")
    private String title;

    @Column(column = "rentalPrice")
    private int rentalPrice;

    @Column(column = "parameter")
    private String parameter;

    @Column(column = "description")
    private String description;

    @Column(column = "carNature")
    private String carNature;

    @Column(column = "carBrandId")
    private int carBrandId;

    public int getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(int carBrandId) {
        this.carBrandId = carBrandId;
    }

    public int getCarLevelId() {
        return carLevelId;
    }

    public void setCarLevelId(int carLevelId) {
        this.carLevelId = carLevelId;
    }

    public int getCarModelsId() {
        return carModelsId;
    }

    public void setCarModelsId(int carModelsId) {
        this.carModelsId = carModelsId;
    }

    public String getCarNature() {
        return carNature;
    }

    public void setCarNature(String carNature) {
        this.carNature = carNature;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public int getMarketRentalPrice() {
        return marketRentalPrice;
    }

    public void setMarketRentalPrice(int marketRentalPrice) {
        this.marketRentalPrice = marketRentalPrice;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(int rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeddingCarRentalId() {
        return weddingCarRentalId;
    }

    public void setWeddingCarRentalId(int weddingCarRentalId) {
        this.weddingCarRentalId = weddingCarRentalId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Car(){}

    public Car(int carBrandId, int carLevelId, int carModelsId, String carNature, String coverUrl, String description, int isUsed, int marketRentalPrice, String parameter, int rentalPrice, String title, int weddingCarRentalId, int weight) {

        this.carBrandId = carBrandId;
        this.carLevelId = carLevelId;
        this.carModelsId = carModelsId;
        this.carNature = carNature;
        this.coverUrl = coverUrl;
        this.description = description;
        this.isUsed = isUsed;
        this.marketRentalPrice = marketRentalPrice;
        this.parameter = parameter;
        this.rentalPrice = rentalPrice;
        this.title = title;
        this.weddingCarRentalId = weddingCarRentalId;
        this.weight = weight;
    }
}
