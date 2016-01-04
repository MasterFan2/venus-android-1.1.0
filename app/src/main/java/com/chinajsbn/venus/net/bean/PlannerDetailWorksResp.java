package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 策划师详情作品列表
 *
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class PlannerDetailWorksResp {

    private String styleName;
    private ArrayList<PlannerWorks> weddingCaseList;

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public ArrayList<PlannerWorks> getWeddingCaseList() {
        return weddingCaseList;
    }

    public void setWeddingCaseList(ArrayList<PlannerWorks> weddingCaseList) {
        this.weddingCaseList = weddingCaseList;
    }

    public PlannerDetailWorksResp(String styleName, ArrayList<PlannerWorks> weddingCaseList) {

        this.styleName = styleName;
        this.weddingCaseList = weddingCaseList;
    }
}
