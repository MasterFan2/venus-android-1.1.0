package com.chinajsbn.venus.net.bean;

/**
 * 详情
 * Created by 13510 on 2015/12/1.
 */
public class OtherDetail {

    private int weddingClassroomId;
    private String title;
    private String content;
    private int viewNumber;
    private int moduleTypeId;
    private int weight;
    private int isUsed;
    private String publishTime;
    private String description;
    private String author;
    private String coverImg;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public OtherDetail(String author, String content, String coverImg, String description, int isUsed, int moduleTypeId, String publishTime, String title, int viewNumber, int weddingClassroomId, int weight) {

        this.author = author;
        this.content = content;
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
