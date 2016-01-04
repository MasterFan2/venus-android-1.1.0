package com.chinajsbn.venus.net.bean;

/**
 * 策划师作品列表
 * <p/>
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class PlannerWorks {
    private String hotelName;
    private double price;
    private int    weddingCaseId;
    private String weddingCaseImage;
    private String weddingCaseStyleName;
    private String weddingDate;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWeddingCaseId() {
        return weddingCaseId;
    }

    public void setWeddingCaseId(int weddingCaseId) {
        this.weddingCaseId = weddingCaseId;
    }

    public String getWeddingCaseImage() {
        return weddingCaseImage;
    }

    public void setWeddingCaseImage(String weddingCaseImage) {
        this.weddingCaseImage = weddingCaseImage;
    }

    public String getWeddingCaseStyleName() {
        return weddingCaseStyleName;
    }

    public void setWeddingCaseStyleName(String weddingCaseStyleName) {
        this.weddingCaseStyleName = weddingCaseStyleName;
    }

    public String getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(String weddingDate) {
        this.weddingDate = weddingDate;
    }

    public PlannerWorks(String hotelName, double price, int weddingCaseId, String weddingCaseImage, String weddingCaseStyleName, String weddingDate) {

        this.hotelName = hotelName;
        this.price = price;
        this.weddingCaseId = weddingCaseId;
        this.weddingCaseImage = weddingCaseImage;
        this.weddingCaseStyleName = weddingCaseStyleName;
        this.weddingDate = weddingDate;
    }
}
