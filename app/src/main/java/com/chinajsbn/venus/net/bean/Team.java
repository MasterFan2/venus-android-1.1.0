package com.chinajsbn.venus.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 团队
 * Created by 13510 on 2015/12/2.
 */
public class Team implements Serializable {

    private PhotoStylistDetail photographerDetail;
    private PhotoStylistDetail stylistDetail;
    private int teamId;
    private int teamLevel;
    private String teamName;
    private String tag;
    private List<Work> workList;

    public Team(PhotoStylistDetail photographerDetail, PhotoStylistDetail stylistDetail, String tag, int teamId, int teamLevel, String teamName, List<Work> workList) {
        this.photographerDetail = photographerDetail;
        this.stylistDetail = stylistDetail;
        this.tag = tag;
        this.teamId = teamId;
        this.teamLevel = teamLevel;
        this.teamName = teamName;
        this.workList = workList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Team() {
    }

    public List<Work> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }

    public PhotoStylistDetail getPhotographerDetail() {
        return photographerDetail;
    }

    public void setPhotographerDetail(PhotoStylistDetail photographerDetail) {
        this.photographerDetail = photographerDetail;
    }

    public PhotoStylistDetail getStylistDetail() {
        return stylistDetail;
    }

    public void setStylistDetail(PhotoStylistDetail stylistDetail) {
        this.stylistDetail = stylistDetail;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTeamLevel() {
        return teamLevel;
    }

    public void setTeamLevel(int teamLevel) {
        this.teamLevel = teamLevel;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
