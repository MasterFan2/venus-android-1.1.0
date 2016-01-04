package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13510 on 2015/10/16.
 * 四大金刚数据
 */
@Table(name = "F4")
public class F4 {

    private int id;

    private List<Emcee> cameraman;//摄像师

    @Column(column = "description")
    private String description;

    private List<Dresser> dresser;
    private List<Emcee> hoster;

    @Column(column = "personGender")
    private String personGender;

    @Column(column = "personId")
    private int personId;

    @Column(column = "personName")
    private String personName;

    @Column(column = "photoUrl")
    private String photoUrl;

    private List<Dresser> photographer;
    private int points;

    @Column(column = "serviceInfo")
    private String serviceInfo;

    @Column(column = "skillPrice")
    private int skillPrice;

    @Column(column = "tag")
    private String tag ;// for cache

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Emcee> getCameraman() {
        return cameraman;
    }

    public void setCameraman(List<Emcee> cameraman) {
        this.cameraman = cameraman;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Dresser> getDresser() {
        return dresser;
    }

    public void setDresser(List<Dresser> dresser) {
        this.dresser = dresser;
    }

    public List<Emcee> getHoster() {
        return hoster;
    }

    public void setHoster(List<Emcee> hoster) {
        this.hoster = hoster;
    }

    public String getPersonGender() {
        return personGender;
    }

    public void setPersonGender(String personGender) {
        this.personGender = personGender;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Dresser> getPhotographer() {
        return photographer;
    }

    public void setPhotographer(List<Dresser> photographer) {
        this.photographer = photographer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public int getSkillPrice() {
        return skillPrice;
    }

    public void setSkillPrice(int skillPrice) {
        this.skillPrice = skillPrice;
    }

    public F4() {}

    public F4(ArrayList<Emcee> cameraman, String description, ArrayList<Dresser> dresser, ArrayList<Emcee> hoster, String personGender, int personId, String personName, String photoUrl, ArrayList<Dresser> photographer, int points, String serviceInfo, int skillPrice) {

        this.cameraman = cameraman;
        this.description = description;
        this.dresser = dresser;
        this.hoster = hoster;
        this.personGender = personGender;
        this.personId = personId;
        this.personName = personName;
        this.photoUrl = photoUrl;
        this.photographer = photographer;
        this.points = points;
        this.serviceInfo = serviceInfo;
        this.skillPrice = skillPrice;
    }
}
