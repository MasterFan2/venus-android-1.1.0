package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 摄影师/造型师 列表
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
@Table(name = "Photographer")
public class Photographer implements Serializable {

    private int id;

    @Column(column = "description")
    private String description;

    @Column(column = "experience")
    private int experience;

    @Column(column = "level")
    private String level;

    @Column(column = "levelId")
    private int levelId;

    private List<PhotographerWorks> list;

    @Column(column = "ownedCompany")
    private String ownedCompany;

    @Column(column = "personId")
    private int personId;

    @Column(column = "personName")
    private String personName;

    @Column(column = "photoUrl")
    private String photoUrl;

    @Column(column = "tag")
    private String tag; //tag 标识为：摄影师总监/摄影师资深/造型师总监/造型师资深

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Photographer(){}

    public Photographer(String description, int experience, String level, int levelId, ArrayList<PhotographerWorks> list, String ownedCompany, int personId, String personName, String photoUrl, String tag) {
        this.description = description;
        this.experience = experience;
        this.level = level;
        this.levelId = levelId;
        this.list = list;
        this.ownedCompany = ownedCompany;
        this.personId = personId;
        this.personName = personName;
        this.photoUrl = photoUrl;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<PhotographerWorks> getList() {
        return list;
    }

    public void setList(List<PhotographerWorks> list) {
        this.list = list;
    }

    public String getOwnedCompany() {
        return ownedCompany;
    }

    public void setOwnedCompany(String ownedCompany) {
        this.ownedCompany = ownedCompany;
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
}
