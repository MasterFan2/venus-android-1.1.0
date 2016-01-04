package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 策划师列表和对应的作品列表
 *
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class PlannerWorksResp {
    private int plannerId;
    private String plannerName;
    private String description;
    private ArrayList<PlannerWorks> list;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<PlannerWorks> getList() {
        return list;
    }

    public void setList(ArrayList<PlannerWorks> list) {
        this.list = list;
    }

    public PlannerWorksResp(int plannerId, String plannerName, String description, ArrayList<PlannerWorks> list) {

        this.plannerId = plannerId;
        this.plannerName = plannerName;
        this.description = description;
        this.list = list;
    }
}
