package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * 酒店标签 优惠。。。
 * Created by 13510 on 2015/8/31.
 */
public class HotelLabel implements Serializable{
    private String lableCode;
    private String lableDesc;
    private String lableId;
    private String lableName;

    public String getLableCode() {
        return lableCode;
    }

    public void setLableCode(String lableCode) {
        this.lableCode = lableCode;
    }

    public String getLableDesc() {
        return lableDesc;
    }

    public void setLableDesc(String lableDesc) {
        this.lableDesc = lableDesc;
    }

    public String getLableId() {
        return lableId;
    }

    public void setLableId(String lableId) {
        this.lableId = lableId;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public HotelLabel(String lableCode, String lableDesc, String lableId, String lableName) {

        this.lableCode = lableCode;
        this.lableDesc = lableDesc;
        this.lableId = lableId;
        this.lableName = lableName;
    }
}
