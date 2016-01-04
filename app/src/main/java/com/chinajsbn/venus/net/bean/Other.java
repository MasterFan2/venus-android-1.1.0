package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 杂项
 * Created by 13510 on 2015/11/30.
 */
@Table(name = "Other")
public class Other implements Serializable{

    @Column(column = "author")
    private String author;

    @Column(column = "viewNumber")
    private int viewNumber;

    @Column(column = "title")
    private String title;

    @Column(column = "coverImg")
    private String coverImg;

    @Column(column = "isUsed")
    private int isUsed;

    @Column(column = "weight")
    private int weight;

    @Column(column = "publishTime")
    private String publishTime;

    @Column(column = "description")
    private String description;

    @Id
    @Column(column = "weddingClassroomId")
    private int weddingClassroomId;

    @Column(column = "moduleTypeId")
    private int moduleTypeId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getModuleTypeId() {
        return moduleTypeId;
    }

    public void setModuleTypeId(int moduleTypeId) {
        this.moduleTypeId = moduleTypeId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getWeddingClassroomId() {
        return weddingClassroomId;
    }

    public void setWeddingClassroomId(int weddingClassroomId) {
        this.weddingClassroomId = weddingClassroomId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Other() {}

    public Other(String author, String coverImg, String description, int isUsed, int moduleTypeId, String publishTime, String title, int viewNumber, int weddingClassroomId, int weight) {
        this.author = author;
        this.coverImg = coverImg;
        this.description = description;
        this.isUsed = isUsed;
        this.moduleTypeId = moduleTypeId;
        this.publishTime = publishTime;
        this.title = title;
        this.viewNumber = viewNumber;
        this.weddingClassroomId = weddingClassroomId;
        this.weight = weight;
    }
}
