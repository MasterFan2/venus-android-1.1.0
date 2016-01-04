package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 婚纱套系详情
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
public class WeddingSuitDetail {

    private int         activityId;
    private ArrayList<DetailSubImg> baseSampleImages;//拍摄基地样片
    private String      cityId;
    private ArrayList<DetailSubImg> clothShootImages;//服装拍摄
    private ArrayList<DetailSubImg> cosmeticImages;//化妆品
    private ArrayList<DetailSubImg> coverImage;//封面图片
    private ArrayList<DetailSubImg> processImages;//流程
    private ArrayList<DetailSubImg> serviceImages;//服务
    private double      depositRate;//定金比例
    private String      description;//套系简介
    private ArrayList<DetailSubImg>    detailImages;//详细页面
    private String      dressModeling;//礼服造型
    private String      dressShoot;//服装拍摄
    private int         isOptionalCameraman;//是否可自选摄影师：可自选 ；0 不可自选
    private int         isOptionalStylist;// 是否可自选造型师：1 可自选 ；0 不可自选
    private int         isSendMV;//是否赠送花絮MV：1 赠送 ；0 不赠送
    private int         isUsed;// 是否有效：1 有效 ； 0 无效
    private int         isValueAddService;//是否增值业务：0 否；1 是 默认值：0
    private int         price;
    private String      productId;
    private String      productName;//套系名称
    private String      provinceId;
    private int         recordNum;//样片刻盘张数
    private String      shootAdress;//拍摄地点
    private String      shootAdressDetail;//拍摄地点详细
    private int         shootSampless;//拍摄样片张数
    private String      shootService;//拍摄服务
    private String      shootServiceDetail;//拍摄服务详细介绍
    private int         shootTime;//拍摄时间/天
    private String      suitContent;// 套系内容
    private int         truingNum;//精修张数
    private String      valueAddServiceFeature;//增值业务特点 用英文“,”分隔
    private String      valueAddServiceSpecialNote;//增值业务特别说明
    private int         weddingDressSuitId;//套系Id

    public WeddingSuitDetail(int activityId, ArrayList<DetailSubImg> baseSampleImages, String cityId, ArrayList<DetailSubImg> clothShootImages, ArrayList<DetailSubImg> cosmeticImages, ArrayList<DetailSubImg> coverImage, ArrayList<DetailSubImg> processImages, ArrayList<DetailSubImg> serviceImages, double depositRate, String description, ArrayList<DetailSubImg> detailImages, String dressModeling, String dressShoot, int isOptionalCameraman, int isOptionalStylist, int isSendMV, int isUsed, int isValueAddService, int price, String productId, String productName, String provinceId, int recordNum, String shootAdress, String shootAdressDetail, int shootSampless, String shootService, String shootServiceDetail, int shootTime, String suitContent, int truingNum, String valueAddServiceFeature, String valueAddServiceSpecialNote, int weddingDressSuitId) {
        this.activityId = activityId;
        this.baseSampleImages = baseSampleImages;
        this.cityId = cityId;
        this.clothShootImages = clothShootImages;
        this.cosmeticImages = cosmeticImages;
        this.coverImage = coverImage;
        this.processImages = processImages;
        this.serviceImages = serviceImages;
        this.depositRate = depositRate;
        this.description = description;
        this.detailImages = detailImages;
        this.dressModeling = dressModeling;
        this.dressShoot = dressShoot;
        this.isOptionalCameraman = isOptionalCameraman;
        this.isOptionalStylist = isOptionalStylist;
        this.isSendMV = isSendMV;
        this.isUsed = isUsed;
        this.isValueAddService = isValueAddService;
        this.price = price;
        this.productId = productId;
        this.productName = productName;
        this.provinceId = provinceId;
        this.recordNum = recordNum;
        this.shootAdress = shootAdress;
        this.shootAdressDetail = shootAdressDetail;
        this.shootSampless = shootSampless;
        this.shootService = shootService;
        this.shootServiceDetail = shootServiceDetail;
        this.shootTime = shootTime;
        this.suitContent = suitContent;
        this.truingNum = truingNum;
        this.valueAddServiceFeature = valueAddServiceFeature;
        this.valueAddServiceSpecialNote = valueAddServiceSpecialNote;
        this.weddingDressSuitId = weddingDressSuitId;
    }

    public int getActivityId() {

        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public ArrayList<DetailSubImg> getBaseSampleImages() {
        return baseSampleImages;
    }

    public void setBaseSampleImages(ArrayList<DetailSubImg> baseSampleImages) {
        this.baseSampleImages = baseSampleImages;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public ArrayList<DetailSubImg> getClothShootImages() {
        return clothShootImages;
    }

    public void setClothShootImages(ArrayList<DetailSubImg> clothShootImages) {
        this.clothShootImages = clothShootImages;
    }

    public ArrayList<DetailSubImg> getCosmeticImages() {
        return cosmeticImages;
    }

    public void setCosmeticImages(ArrayList<DetailSubImg> cosmeticImages) {
        this.cosmeticImages = cosmeticImages;
    }

    public ArrayList<DetailSubImg> getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(ArrayList<DetailSubImg> coverImage) {
        this.coverImage = coverImage;
    }

    public ArrayList<DetailSubImg> getProcessImages() {
        return processImages;
    }

    public void setProcessImages(ArrayList<DetailSubImg> processImages) {
        this.processImages = processImages;
    }

    public ArrayList<DetailSubImg> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(ArrayList<DetailSubImg> serviceImages) {
        this.serviceImages = serviceImages;
    }

    public double getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(double depositRate) {
        this.depositRate = depositRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DetailSubImg> getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(ArrayList<DetailSubImg> detailImages) {
        this.detailImages = detailImages;
    }

    public String getDressModeling() {
        return dressModeling;
    }

    public void setDressModeling(String dressModeling) {
        this.dressModeling = dressModeling;
    }

    public String getDressShoot() {
        return dressShoot;
    }

    public void setDressShoot(String dressShoot) {
        this.dressShoot = dressShoot;
    }

    public int getIsOptionalCameraman() {
        return isOptionalCameraman;
    }

    public void setIsOptionalCameraman(int isOptionalCameraman) {
        this.isOptionalCameraman = isOptionalCameraman;
    }

    public int getIsOptionalStylist() {
        return isOptionalStylist;
    }

    public void setIsOptionalStylist(int isOptionalStylist) {
        this.isOptionalStylist = isOptionalStylist;
    }

    public int getIsSendMV() {
        return isSendMV;
    }

    public void setIsSendMV(int isSendMV) {
        this.isSendMV = isSendMV;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getIsValueAddService() {
        return isValueAddService;
    }

    public void setIsValueAddService(int isValueAddService) {
        this.isValueAddService = isValueAddService;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public String getShootAdress() {
        return shootAdress;
    }

    public void setShootAdress(String shootAdress) {
        this.shootAdress = shootAdress;
    }

    public String getShootAdressDetail() {
        return shootAdressDetail;
    }

    public void setShootAdressDetail(String shootAdressDetail) {
        this.shootAdressDetail = shootAdressDetail;
    }

    public int getShootSampless() {
        return shootSampless;
    }

    public void setShootSampless(int shootSampless) {
        this.shootSampless = shootSampless;
    }

    public String getShootService() {
        return shootService;
    }

    public void setShootService(String shootService) {
        this.shootService = shootService;
    }

    public String getShootServiceDetail() {
        return shootServiceDetail;
    }

    public void setShootServiceDetail(String shootServiceDetail) {
        this.shootServiceDetail = shootServiceDetail;
    }

    public int getShootTime() {
        return shootTime;
    }

    public void setShootTime(int shootTime) {
        this.shootTime = shootTime;
    }

    public String getSuitContent() {
        return suitContent;
    }

    public void setSuitContent(String suitContent) {
        this.suitContent = suitContent;
    }

    public int getTruingNum() {
        return truingNum;
    }

    public void setTruingNum(int truingNum) {
        this.truingNum = truingNum;
    }

    public String getValueAddServiceFeature() {
        return valueAddServiceFeature;
    }

    public void setValueAddServiceFeature(String valueAddServiceFeature) {
        this.valueAddServiceFeature = valueAddServiceFeature;
    }

    public String getValueAddServiceSpecialNote() {
        return valueAddServiceSpecialNote;
    }

    public void setValueAddServiceSpecialNote(String valueAddServiceSpecialNote) {
        this.valueAddServiceSpecialNote = valueAddServiceSpecialNote;
    }

    public int getWeddingDressSuitId() {
        return weddingDressSuitId;
    }

    public void setWeddingDressSuitId(int weddingDressSuitId) {
        this.weddingDressSuitId = weddingDressSuitId;
    }
}
