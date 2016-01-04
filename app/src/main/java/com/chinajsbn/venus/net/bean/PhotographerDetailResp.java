package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 摄影师详情
 *
 * Created by MasterFan on 2015/6/19.
 * description:
 */
public class PhotographerDetailResp {

    private String  description;     //描述
    private int     experence;       //从业经验
    private String  level;           //职称
    private ArrayList<PhotographerWorks> list;//作品列表
    private String  ownedCompany;    //所属公司
    private int     personId;        //ID
    private String  personName;      //人名
    private float   points;          //评分

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExperence() {
        return experence;
    }

    public void setExperence(int experence) {
        this.experence = experence;
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

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public PhotographerDetailResp(String description, int experence, String level, ArrayList<PhotographerWorks> list, String ownedCompany, int personId, String personName, float points) {

        this.description = description;
        this.experence = experence;
        this.level = level;
        this.list = list;
        this.ownedCompany = ownedCompany;
        this.personId = personId;
        this.personName = personName;
        this.points = points;
    }
}
