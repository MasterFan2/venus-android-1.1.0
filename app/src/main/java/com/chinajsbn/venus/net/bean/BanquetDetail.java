package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/9/24.
 */
public class BanquetDetail {
    private String[] detailImgList;
    private String latitude;
    private String longitude;

    public String[] getDetailImgList() {
        return detailImgList;
    }

    public void setDetailImgList(String[] detailImgList) {
        this.detailImgList = detailImgList;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public BanquetDetail(String[] detailImgList, String latitude, String longitude) {

        this.detailImgList = detailImgList;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
