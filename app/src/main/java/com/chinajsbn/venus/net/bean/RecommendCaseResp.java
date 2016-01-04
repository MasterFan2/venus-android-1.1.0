package com.chinajsbn.venus.net.bean;

/**
 * 推荐案例
 *
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class RecommendCaseResp {

    private int weddingCaseId;
    private String weddingCaseImage;//案例封面图
    private String weddingDate;     //案例time
    private int weight;

    public int getWeddingCaseId() {
        return weddingCaseId;
    }

    public void setWeddingCaseId(int weddingCaseId) {
        this.weddingCaseId = weddingCaseId;
    }

    public String getWeddingCaseImage() {
        return weddingCaseImage;
    }

    public void setWeddingCaseImage(String weddingCaseImage) {
        this.weddingCaseImage = weddingCaseImage;
    }

    public String getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(String weddingDate) {
        this.weddingDate = weddingDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public RecommendCaseResp(int weddingCaseId, String weddingCaseImage, String weddingDate, int weight) {

        this.weddingCaseId = weddingCaseId;
        this.weddingCaseImage = weddingCaseImage;
        this.weddingDate = weddingDate;
        this.weight = weight;
    }
}
