package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * 酒店列表
 * Created by 13510 on 2015/8/27.
 */
@Table(name = "Hotel")
public class Hotel {

    @Column(column = "address")
    private String address;

    @Column(column = "banquetHallCount")
    private String banquetHallCount;

    private ArrayList<Banquet> banquetHallList;

    @Column(column = "capacityPerTable")
    private int capacityPerTable;

    @Column(column = "featureLable")
    private String featureLable;

    @Column(column = "highestConsumption")
    private int highestConsumption;

    @Id
    @Column(column = "hotelId")
    private String hotelId;

    @Column(column = "hotelName")
    private String hotelName;

    @Column(column = "imageUrl")
    private String imageUrl;

    @Column(column = "isDiscount")
    private String isDiscount;

    @Column(column = "isGift")
    private String isGift;

    @Column(column = "listCount")
    private String listCount;

    @Column(column = "lowestConsumption")
    private String lowestConsumption;

    @Column(column = "typeName")
    private String typeName;

    @Column(column = "cityId")
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBanquetHallCount() {
        return banquetHallCount;
    }

    public void setBanquetHallCount(String banquetHallCount) {
        this.banquetHallCount = banquetHallCount;
    }

    public ArrayList<Banquet> getBanquetHallList() {
        return banquetHallList;
    }

    public void setBanquetHallList(ArrayList<Banquet> banquetHallList) {
        this.banquetHallList = banquetHallList;
    }

    public int getCapacityPerTable() {
        return capacityPerTable;
    }

    public void setCapacityPerTable(int capacityPerTable) {
        this.capacityPerTable = capacityPerTable;
    }

    public String getFeatureLable() {
        return featureLable;
    }

    public void setFeatureLable(String featureLable) {
        this.featureLable = featureLable;
    }

    public int getHighestConsumption() {
        return highestConsumption;
    }

    public void setHighestConsumption(int highestConsumption) {
        this.highestConsumption = highestConsumption;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift;
    }

    public String getListCount() {
        return listCount;
    }

    public void setListCount(String listCount) {
        this.listCount = listCount;
    }

    public String getLowestConsumption() {
        return lowestConsumption;
    }

    public void setLowestConsumption(String lowestConsumption) {
        this.lowestConsumption = lowestConsumption;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Hotel(){}

    public Hotel(String address, String banquetHallCount, ArrayList<Banquet> banquetHallList, int capacityPerTable, String featureLable, int highestConsumption, String hotelId, String hotelName, String imageUrl, String isDiscount, String isGift, String listCount, String lowestConsumption, String typeName) {

        this.address = address;
        this.banquetHallCount = banquetHallCount;
        this.banquetHallList = banquetHallList;
        this.capacityPerTable = capacityPerTable;
        this.featureLable = featureLable;
        this.highestConsumption = highestConsumption;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.imageUrl = imageUrl;
        this.isDiscount = isDiscount;
        this.isGift = isGift;
        this.listCount = listCount;
        this.lowestConsumption = lowestConsumption;
        this.typeName = typeName;
    }
}
