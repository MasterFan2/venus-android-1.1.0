package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 策划师详情
 *
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class PlannerDetailResp {
    private String  description;//
    private int     experence;  //从业年限
    private String  level;      //级别
    private ArrayList<PlannerDetailWorks> list;
    private String  ownedCompany;//所属公司
    private int     plannerId;
    private String  plannerName;
    private float   points;     //评分

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

    public ArrayList<PlannerDetailWorks> getList() {
        return list;
    }

    public void setList(ArrayList<PlannerDetailWorks> list) {
        this.list = list;
    }

    public String getOwnedCompany() {
        return ownedCompany;
    }

    public void setOwnedCompany(String ownedCompany) {
        this.ownedCompany = ownedCompany;
    }

    public int getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(int plannerId) {
        this.plannerId = plannerId;
    }

    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String plannerName) {
        this.plannerName = plannerName;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public PlannerDetailResp(String description, int experence, String level, ArrayList<PlannerDetailWorks> list, String ownedCompany, int plannerId, String plannerName, float points) {

        this.description = description;
        this.experence = experence;
        this.level = level;
        this.list = list;
        this.ownedCompany = ownedCompany;
        this.plannerId = plannerId;
        this.plannerName = plannerName;
        this.points = points;
    }
}
