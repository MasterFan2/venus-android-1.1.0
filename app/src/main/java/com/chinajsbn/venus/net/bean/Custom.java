package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 样片 /客片
 *
 * Created by MasterFan on 2015/6/26.
 * description:
 */
@Table(name = "Custom")
public class Custom implements Serializable {

    private int id;

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

    @Column(column = "contentId")
    private String contentId;

    @Column(column = "contentName")
    private String contentName;

    @Column(column = "contentUrl")
    private String contentUrl;

    @Column(column = "createDate")
    private String createDate;

    @Column(column = "shootingStyleId")
    private String shootingStyleId;

    @Column(column = "shootingStyleName")
    private String shootingStyleName;

    @Foreign(column = "photographerId", foreign = "personId")
    private SimplePhotographer photographer;

    @Column(column = "seasonId")
    private String seasonId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    private ArrayList<SimpleStyles> shootingStyles;

    @Foreign(column = "stylistId", foreign = "personId")
    private SimplePhotographer stylist;

    public String getShootingStyleId() {
        return shootingStyleId;
    }

    public void setShootingStyleId(String shootingStyleId) {
        this.shootingStyleId = shootingStyleId;
    }

    public String getShootingStyleName() {
        return shootingStyleName;
    }

    public void setShootingStyleName(String shootingStyleName) {
        this.shootingStyleName = shootingStyleName;
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

    public Custom() {

    }

    public Custom(String actorNameFemale, String actorNameMale, String contentId, String contentName, String contentUrl, String createDate) {
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

