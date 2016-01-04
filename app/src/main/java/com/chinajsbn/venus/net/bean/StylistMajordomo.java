package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 摄影师总监
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
@Table(name = "StylistMajordomo")
public class StylistMajordomo implements Serializable {

    @Column(column = "description")
    private String description;

    @Column(column = "experience")
    private int experience;

    @Column(column = "level")
    private String level;

    @Column(column = "levelId")
    private int levelId;

    private ArrayList<PhotographerWorks> list;

    @Column(column = "ownedCompany")
    private String ownedCompany;

    @Id
    @Column(column = "personId")
    private int personId;

    @Column(column = "personName")
    private String personName;

    @Column(column = "photoUrl")
    private String photoUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StylistMajordomo(){}

    public StylistMajordomo(String description, int experience, String level, int levelId, ArrayList<PhotographerWorks> list, String ownedCompany, int personId, String personName, String photoUrl) {

        this.description = description;
        this.experience = experience;
        this.level = level;
        this.levelId = levelId;
        this.list = list;
        this.ownedCompany = ownedCompany;
        this.personId = personId;
        this.personName = personName;
        this.photoUrl = photoUrl;
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

    public ArrayList<PhotographerWorks> getList() {
        return list;
    }

    public void setList(ArrayList<PhotographerWorks> list) {
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

    public StylistMajordomo(int experience, String level, int levelId, ArrayList<PhotographerWorks> list, String ownedCompany, int personId, String personName, String photoUrl) {
        this.experience = experience;
        this.level = level;
        this.levelId = levelId;
        this.list = list;
        this.ownedCompany = ownedCompany;
        this.personId = personId;
        this.personName = personName;
        this.photoUrl = photoUrl;
    }
}
