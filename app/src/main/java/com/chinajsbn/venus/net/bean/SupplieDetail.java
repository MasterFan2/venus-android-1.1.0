package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/12/15.
 */
public class SupplieDetail {
    private int weddingSuppliesId;
    private String title;
    private String number;
    private String suppliesNumber;
    private String parameter;
    private String description;
    private String detail;
    private String brand;
    private String[] detailPics;

    public SupplieDetail(String brand, String description, String detail, String[] detailPics, String number, String parameter, String suppliesNumber, String title, int weddingSuppliesId) {
        this.brand = brand;
        this.description = description;
        this.detail = detail;
        this.detailPics = detailPics;
        this.number = number;
        this.parameter = parameter;
        this.suppliesNumber = suppliesNumber;
        this.title = title;
        this.weddingSuppliesId = weddingSuppliesId;
    }

    public String getBrand() {

        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getSuppliesNumber() {
        return suppliesNumber;
    }

    public void setSuppliesNumber(String suppliesNumber) {
        this.suppliesNumber = suppliesNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeddingSuppliesId() {
        return weddingSuppliesId;
    }

    public void setWeddingSuppliesId(int weddingSuppliesId) {
        this.weddingSuppliesId = weddingSuppliesId;
    }
}
