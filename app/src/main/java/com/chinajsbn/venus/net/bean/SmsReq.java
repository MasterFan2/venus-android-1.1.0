package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/4/25.
 * description:
 */
public class SmsReq {
    private String USERNAME ;
    private int TYPE ;

    @Override
    public String toString() {
        return "SmsReq{" +
                "USERNAME='" + USERNAME + '\'' +
                ", TYPE=" + TYPE +
                '}';
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public SmsReq(String USERNAME, int TYPE) {

        this.USERNAME = USERNAME;
        this.TYPE = TYPE;
    }
}
