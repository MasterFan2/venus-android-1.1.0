package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * 案例详情
 * Created by 13510 on 2015/12/8.
 */
public class PlanDetail {
    private String banquetHallName;//宴会厅
    private String coverImageUrl;
    private int hdpcCost;       //四大金刚费用（婚礼人费用）
    private String hotelName;
    private List<ImageItem> imageList;
    private int originalPrice; //原价
    private int sceneCost;          //场景布置费用
    private String schemeColor;//案例主题
    private String schemeDesc;
    private String schemeName;//案例名称
    private List<Style> schemeStyles;
    private String standardWedding; //标配婚礼人 1：主持人  2:化妆师  3：摄影师  4：摄像师    5：双机摄影    6：双机摄像     用英文“,”分隔
    private int totalPrice;// 折后价
    private String weddingCaseId;
    private String weddingDate;

    public String getBanquetHallName() {
        return banquetHallName;
    }

    public void setBanquetHallName(String banquetHallName) {
        this.banquetHallName = banquetHallName;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public int getHdpcCost() {
        return hdpcCost;
    }

    public void setHdpcCost(int hdpcCost) {
        this.hdpcCost = hdpcCost;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public List<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getSceneCost() {
        return sceneCost;
    }

    public void setSceneCost(int sceneCost) {
        this.sceneCost = sceneCost;
    }

    public String getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(String schemeColor) {
        this.schemeColor = schemeColor;
    }

    public String getSchemeDesc() {
        return schemeDesc;
    }

    public void setSchemeDesc(String schemeDesc) {
        this.schemeDesc = schemeDesc;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public List<Style> getSchemeStyles() {
        return schemeStyles;
    }

    public void setSchemeStyles(List<Style> schemeStyles) {
        this.schemeStyles = schemeStyles;
    }

    public String getStandardWedding() {
        return standardWedding;
    }

    public void setStandardWedding(String standardWedding) {
        this.standardWedding = standardWedding;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getWeddingCaseId() {
        return weddingCaseId;
    }

    public void setWeddingCaseId(String weddingCaseId) {
        this.weddingCaseId = weddingCaseId;
    }

    public String getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(String weddingDate) {
        this.weddingDate = weddingDate;
    }

    public PlanDetail(String banquetHallName, String coverImageUrl, int hdpcCost, String hotelName, List<ImageItem> imageList, int originalPrice, int sceneCost, String schemeColor, String schemeDesc, String schemeName, List<Style> schemeStyles, String standardWedding, int totalPrice, String weddingCaseId, String weddingDate) {

        this.banquetHallName = banquetHallName;
        this.coverImageUrl = coverImageUrl;
        this.hdpcCost = hdpcCost;
        this.hotelName = hotelName;
        this.imageList = imageList;
        this.originalPrice = originalPrice;
        this.sceneCost = sceneCost;
        this.schemeColor = schemeColor;
        this.schemeDesc = schemeDesc;
        this.schemeName = schemeName;
        this.schemeStyles = schemeStyles;
        this.standardWedding = standardWedding;
        this.totalPrice = totalPrice;
        this.weddingCaseId = weddingCaseId;
        this.weddingDate = weddingDate;
    }
}
