package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import org.intellij.lang.annotations.Identifier;

import java.io.Serializable;

/**
 * 婚纱套系
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
@Table(name = "WeddingSuit")
public class WeddingSuit implements Serializable{

    @Column(column = "activityId")
    private int activityId;     //活动Id

    @Column(column = "cityId")
    private int cityId;         //城市ID

    @Column(column = "depositRate")
    private int depositRate;    //价格比例

    @Column(column = "description")
    private String description; //套系简介

    @Column(column = "dressModeling")
    private String dressModeling;//礼服造型

    @Column(column = "imageUrl")
    private String imageUrl;

    @Column(column = "isOptionalCameraman")
    private int isOptionalCameraman;    //是否可自选摄影师：可自选 ；0 不可自选

    @Column(column = "isOptionalStylist")
    private int isOptionalStylist;      //是否可自选造型师：1 可自选 ；0 不可自选

    @Column(column = "isSendMV")
    private int isSendMV;               //是否赠送花絮MV：1 赠送 ；0 不赠送

    @Column(column = "isUsed")
    private int isUsed;                 //是否有效：1 有效 ； 0 无效

    @Column(column = "isValueAddService")
    private int isValueAddService;      //是否增值业务：0 否；1 是 默认值：0

    @Column(column = "price")
    private int price;                  //价钱

    @Id
    @Column(column = "productId")
    private String productId;           //套系ID

    @Column(column = "productName")
    private String productName;         //套系名称

    @Column(column = "provinceId")
    private int provinceId;             //省ID

    @Column(column = "recordNum")
    private int recordNum;              //样片刻盘张数

    @Column(column = "shootAdress")
    private String shootAdress;         //拍摄地点

    @Column(column = "shootSampless")
    private int shootSampless;          //拍摄样片张数

    @Column(column = "shootService")
    private String shootService;        //拍摄服务描述

    @Column(column = "shootTime")
    private int shootTime;              //拍摄时间/天

    @Column(column = "truingNum")
    private int truingNum;              //精修张数

    @Column(column = "valueAddServiceFeature")
    private String valueAddServiceFeature;      //增值业务特点 用英文“,”分隔

    @Column(column = "valueAddServiceSpecialNote")
    private String valueAddServiceSpecialNote;  //增值业务特别说明

    @Column(column = "weddingDressSuitId")
    private int weddingDressSuitId;             //婚纱套系Id

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(int depositRate) {
        this.depositRate = depositRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDressModeling() {
        return dressModeling;
    }

    public void setDressModeling(String dressModeling) {
        this.dressModeling = dressModeling;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
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

    public int getShootTime() {
        return shootTime;
    }

    public void setShootTime(int shootTime) {
        this.shootTime = shootTime;
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

    public WeddingSuit() {}

    public WeddingSuit(int activityId, int cityId, int depositRate, String description, String dressModeling, String imageUrl, int isOptionalCameraman, int isOptionalStylist, int isSendMV, int isUsed, int isValueAddService, int price, String productId, String productName, int provinceId, int recordNum, String shootAdress, int shootSampless, String shootService, int shootTime, int truingNum, String valueAddServiceFeature, String valueAddServiceSpecialNote, int weddingDressSuitId) {

        this.activityId = activityId;
        this.cityId = cityId;
        this.depositRate = depositRate;
        this.description = description;
        this.dressModeling = dressModeling;
        this.imageUrl = imageUrl;
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
        this.shootSampless = shootSampless;
        this.shootService = shootService;
        this.shootTime = shootTime;
        this.truingNum = truingNum;
        this.valueAddServiceFeature = valueAddServiceFeature;
        this.valueAddServiceSpecialNote = valueAddServiceSpecialNote;
        this.weddingDressSuitId = weddingDressSuitId;
    }
}
