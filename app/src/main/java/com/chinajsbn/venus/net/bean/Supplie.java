package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/12/15.
 */
@Table(name = "Supplie")
public class Supplie {

    @Column(column = "sellingPrice")
    private String sellingPrice;

    @Column(column = "marketPrice")
    private String marketPrice;

    @Column(column = "title")
    private String title;

    @Column(column = "weight")
    private int weight;

    @Column(column = "isUsed")
    private int isUsed;

    @Column(column = "coverUrl")
    private String coverUrl;

    @Column(column = "suppliesNumber")
    private String suppliesNumber;

    @Column(column = "description")
    private String description;

    @Column(column = "number")
    private String number;

    @Column(column = "weddingSuppliesTypeId")
    private int weddingSuppliesTypeId;

    @Column(column = "brandId")
    private int brandId;

    @Id
    @Column(column = "weddingSuppliesId")
    private int weddingSuppliesId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
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

    public int getWeddingSuppliesTypeId() {
        return weddingSuppliesTypeId;
    }

    public void setWeddingSuppliesTypeId(int weddingSuppliesTypeId) {
        this.weddingSuppliesTypeId = weddingSuppliesTypeId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Supplie(){}

    public Supplie(int brandId, String coverUrl, String description, int isUsed, String marketPrice, String number, String sellingPrice, String suppliesNumber, String title, int weddingSuppliesId, int weddingSuppliesTypeId, int weight) {

        this.brandId = brandId;
        this.coverUrl = coverUrl;
        this.description = description;
        this.isUsed = isUsed;
        this.marketPrice = marketPrice;
        this.number = number;
        this.sellingPrice = sellingPrice;
        this.suppliesNumber = suppliesNumber;
        this.title = title;
        this.weddingSuppliesId = weddingSuppliesId;
        this.weddingSuppliesTypeId = weddingSuppliesTypeId;
        this.weight = weight;
    }
}
