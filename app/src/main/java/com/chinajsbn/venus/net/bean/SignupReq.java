package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/4/25.
 * description:
 */
public class SignupReq {
    private String USERNAME;
    private String CODE;
    private int PLATFORM;//0-web 1-app 2-3D

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public int getPLATFORM() {
        return PLATFORM;
    }

    public void setPLATFORM(int PLATFORM) {
        this.PLATFORM = PLATFORM;
    }

    public SignupReq(String USERNAME, String CODE, int PLATFORM) {
        this.USERNAME = USERNAME;
        this.CODE = CODE;
        this.PLATFORM = PLATFORM;

    }
}
