package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 宴会厅
 * Created by 13510 on 2015/8/27.
 */
@Table(name = "Banquet")
public class Banquet implements Serializable {

    @Column(column = "area")
    private String area;

    private ArrayList<String> banquetHallImage;

    @Column(column = "image_url")
    private String image_url;

    @Column(column = "leastConsumption")
    private String leastConsumption;

    @Column(column = "shape")
    private String shape;

    @Id
    @Column(column = "banquetHallId")
    private String banquetHallId;

    @Column(column = "banquetHallName")
    private String banquetHallName;

    @Column(column = "capacity")
    private String capacity;

    @Column(column = "height")
    private String height;

    @Column(column = "hotelId")
    private String hotelId;

    @Column(column = "pillarNumber")
    private String pillarNumber;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ArrayList<String> getBanquetHallImage() {
        return banquetHallImage;
    }

    public void setBanquetHallImage(ArrayList<String> banquetHallImage) {
        this.banquetHallImage = banquetHallImage;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLeastConsumption() {
        return leastConsumption;
    }

    public void setLeastConsumption(String leastConsumption) {
        this.leastConsumption = leastConsumption;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getBanquetHallId() {
        return banquetHallId;
    }

    public void setBanquetHallId(String banquetHallId) {
        this.banquetHallId = banquetHallId;
    }

    public String getBanquetHallName() {
        return banquetHallName;
    }

    public void setBanquetHallName(String banquetHallName) {
        this.banquetHallName = banquetHallName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getPillarNumber() {
        return pillarNumber;
    }

    public void setPillarNumber(String pillarNumber) {
        this.pillarNumber = pillarNumber;
    }
    public Banquet(){}
    public Banquet(String area, ArrayList<String> banquetHallImage, String image_url, String leastConsumption, String shape, String banquetHallId, String banquetHallName, String capacity, String height, String hotelId, String pillarNumber) {

        this.area = area;
        this.banquetHallImage = banquetHallImage;
        this.image_url = image_url;
        this.leastConsumption = leastConsumption;
        this.shape = shape;
        this.banquetHallId = banquetHallId;
        this.banquetHallName = banquetHallName;
        this.capacity = capacity;
        this.height = height;
        this.hotelId = hotelId;
        this.pillarNumber = pillarNumber;
    }
}
