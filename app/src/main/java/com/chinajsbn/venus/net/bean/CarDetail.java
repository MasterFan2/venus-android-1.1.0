package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/12/14.
 */
public class CarDetail {
    private int weddingCarRentalId;
    private String title;
    private String description;
    private String parameter;
    private String detail;
    private int marketRentalPrice;
    private int rentalPrice;
    private int carLevelId;
    private int carModelsId;
    private int carBrandId;
    private int carNature;
    private String[] detailPics;

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

    public int getCarNature() {
        return carNature;
    }

    public void setCarNature(int carNature) {
        this.carNature = carNature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String[] getDetailPics() {
        return detailPics;
    }

    public void setDetailPics(String[] detailPics) {
        this.detailPics = detailPics;
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

    public CarDetail(int carBrandId, int carLevelId, int carModelsId, int carNature, String description, String detail, String[] detailPics, int marketRentalPrice, String parameter, int rentalPrice, String title, int weddingCarRentalId) {

        this.carBrandId = carBrandId;
        this.carLevelId = carLevelId;
        this.carModelsId = carModelsId;
        this.carNature = carNature;
        this.description = description;
        this.detail = detail;
        this.detailPics = detailPics;
        this.marketRentalPrice = marketRentalPrice;
        this.parameter = parameter;
        this.rentalPrice = rentalPrice;
        this.title = title;
        this.weddingCarRentalId = weddingCarRentalId;
    }
}
