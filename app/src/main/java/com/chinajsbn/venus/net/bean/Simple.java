package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 样片 /客片
 *
 * Created by MasterFan on 2015/6/26.
 * description:
 */
@Table(name = "Simple")
public class Simple implements Serializable {

    @Column(column = "headPhotographyUrl")
    private String headPhotographyUrl;

    @Column(column = "headPhotographyName")
    private String headPhotographyName;

    @Column(column = "headStylistUrl")
    private String headStylistUrl;

    @Column(column = "headStylistName")
    private String headStylistName;

    @Column(column = "headStyles")
    private String headStyles;

    @Column(column = "actorNameFemale")
    private String actorNameFemale;

    @Column(column = "actorNameMale")
    private String actorNameMale;

    @Id
    @Column(column = "contentId")
    private String contentId;

    @Column(column = "contentName")
    private String contentName;

    @Column(column = "contentUrl")
    private String contentUrl;

    @Column(column = "createDate")
    private String createDate;

    @Foreign(column = "photographerId", foreign = "personId")
    private SimplePhotographer photographer;

    ////////////////cache/////////////////////
    @Column(column = "styleId")
    private String styleId;

    @Column(column = "styleName")
    private String styleName;

    @Column(column = "addressId")
    private String addressId;

    @Column(column = "addressName")
    private String addressName;

    private List<ShootingExteriors> shootingExteriors;

    public List<ShootingExteriors> getShootingExteriors() {
        return shootingExteriors;
    }

    public void setShootingExteriors(List<ShootingExteriors> shootingExteriors) {
        this.shootingExteriors = shootingExteriors;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    //    @Finder(valueColumn = "shootingStylesId", targetColumn = "shootingStyleId")
    private ArrayList<SimpleStyles> shootingStyles;

    @Foreign(column = "stylistId", foreign = "personId")
    private SimplePhotographer stylist;

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public SimplePhotographer getPhotographer() {
        return photographer;
    }

    public void setPhotographer(SimplePhotographer photographer) {
        this.photographer = photographer;
    }

    public String getHeadPhotographyUrl() {
        return headPhotographyUrl;
    }

    public void setHeadPhotographyUrl(String headPhotographyUrl) {
        this.headPhotographyUrl = headPhotographyUrl;
    }

    public String getHeadPhotographyName() {
        return headPhotographyName;
    }

    public void setHeadPhotographyName(String headPhotographyName) {
        this.headPhotographyName = headPhotographyName;
    }

    public String getHeadStylistUrl() {
        return headStylistUrl;
    }

    public void setHeadStylistUrl(String headStylistUrl) {
        this.headStylistUrl = headStylistUrl;
    }

    public String getHeadStylistName() {
        return headStylistName;
    }

    public void setHeadStylistName(String headStylistName) {
        this.headStylistName = headStylistName;
    }

    public String getHeadStyles() {
        return headStyles;
    }

    public void setHeadStyles(String headStyles) {
        this.headStyles = headStyles;
    }

    public ArrayList<SimpleStyles> getShootingStyles() {
        return shootingStyles;
    }

    public void setShootingStyles(ArrayList<SimpleStyles> shootingStyles) {
        this.shootingStyles = shootingStyles;
    }

    public SimplePhotographer getStylist() {
        return stylist;
    }

    public void setStylist(SimplePhotographer stylist) {
        this.stylist = stylist;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Simple() {

    }

    public Simple(String actorNameFemale, String actorNameMale, String contentId, String contentName, String contentUrl, String createDate) {
        this.actorNameFemale = actorNameFemale;
        this.actorNameMale = actorNameMale;
        this.contentId = contentId;
        this.contentName = contentName;
        this.contentUrl = contentUrl;
        this.createDate = createDate;
    }

    public String getActorNameFemale() {

        return actorNameFemale;
    }

    public void setActorNameFemale(String actorNameFemale) {
        this.actorNameFemale = actorNameFemale;
    }

    public String getActorNameMale() {
        return actorNameMale;
    }

    public void setActorNameMale(String actorNameMale) {
        this.actorNameMale = actorNameMale;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
