package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/9/6.
 */
@Table(name = "Scheme")
public class Scheme {

    @Column(column = "schemeName")
    private String schemeName;

    @Column(column = "styleName")
    private String styleName;

    @Column(column = "totalPrice")
    private int totalPrice;

    @Id
    @Column(column = "weddingCaseId")
    private int weddingCaseId;

    @Column(column = "weddingCaseImage")
    private String weddingCaseImage;

    @Column(column = "weddingDate")
    private String weddingDate;

    @Column(column = "weight")
    private int weight;

    @Column(column = "tag")
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(String weddingDate) {
        this.weddingDate = weddingDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Scheme(){}

    public Scheme(String schemeName, int totalPrice, int weddingCaseId, String weddingCaseImage, String weddingDate, int weight) {

        this.schemeName = schemeName;
        this.totalPrice = totalPrice;
        this.weddingCaseId = weddingCaseId;
        this.weddingCaseImage = weddingCaseImage;
        this.weddingDate = weddingDate;
        this.weight = weight;
    }
}
