package com.chinajsbn.venus.net.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * hotel detail
 * Created by masterFan on 2015/8/31.
 */
public class HotelDetail implements Serializable {
    private String address;
    private ArrayList<Banquet> banquetHallList;
    private String capacityPerTable;
    private String cityId;
    private String detailedIntroduction;
    private String highestConsumption;
    private String hotelId;
    private ArrayList<HotelLabel> hotelLabelList;
    private ArrayList<Combo> hotelMealPackList;
    private String hotelName;
    private String[] imageUrlList;
    private String latitude;
    private String longitude;
    private String lowestConsumption;
    private String typeName;

    @Override
    public String toString() {
        return "HotelDetail{" +
                "address='" + address + '\'' +
                ", banquetHallList=" + banquetHallList +
                ", capacityPerTable='" + capacityPerTable + '\'' +
                ", cityId='" + cityId + '\'' +
                ", detailedIntroduction='" + detailedIntroduction + '\'' +
                ", highestConsumption='" + highestConsumption + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", hotelLabelList=" + hotelLabelList +
                ", hotelMealPackList=" + hotelMealPackList +
                ", hotelName='" + hotelName + '\'' +
                ", imageUrlList=" + Arrays.toString(imageUrlList) +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", lowestConsumption='" + lowestConsumption + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Banquet> getBanquetHallList() {
        return banquetHallList;
    }

    public void setBanquetHallList(ArrayList<Banquet> banquetHallList) {
        this.banquetHallList = banquetHallList;
    }

    public String getCapacityPerTable() {
        return capacityPerTable;
    }

    public void setCapacityPerTable(String capacityPerTable) {
        this.capacityPerTable = capacityPerTable;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDetailedIntroduction() {
        return detailedIntroduction;
    }

    public void setDetailedIntroduction(String detailedIntroduction) {
        this.detailedIntroduction = detailedIntroduction;
    }

    public String getHighestConsumption() {
        return highestConsumption;
    }

    public void setHighestConsumption(String highestConsumption) {
        this.highestConsumption = highestConsumption;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public ArrayList<HotelLabel> getHotelLabelList() {
        return hotelLabelList;
    }

    public void setHotelLabelList(ArrayList<HotelLabel> hotelLabelList) {
        this.hotelLabelList = hotelLabelList;
    }

    public ArrayList<Combo> getHotelMealPackList() {
        return hotelMealPackList;
    }

    public void setHotelMealPackList(ArrayList<Combo> hotelMealPackList) {
        this.hotelMealPackList = hotelMealPackList;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String[] getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String[] imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public HotelDetail(String address, ArrayList<Banquet> banquetHallList, String capacityPerTable, String cityId, String detailedIntroduction, String highestConsumption, String hotelId, ArrayList<HotelLabel> hotelLabelList, ArrayList<Combo> hotelMealPackList, String hotelName, String[] imageUrlList, String latitude, String longitude, String lowestConsumption, String typeName) {

        this.address = address;
        this.banquetHallList = banquetHallList;
        this.capacityPerTable = capacityPerTable;
        this.cityId = cityId;
        this.detailedIntroduction = detailedIntroduction;
        this.highestConsumption = highestConsumption;
        this.hotelId = hotelId;
        this.hotelLabelList = hotelLabelList;
        this.hotelMealPackList = hotelMealPackList;
        this.hotelName = hotelName;
        this.imageUrlList = imageUrlList;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lowestConsumption = lowestConsumption;
        this.typeName = typeName;
    }
}
