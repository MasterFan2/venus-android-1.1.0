package com.chinajsbn.venus.net.bean;

/**
 * Created by chenjianjun on 15-4-14.
 * <p/>
 * Beyond their own, let the future
 */
public class LoginReq {

    // 登录用户名
    private String USERNAME;
    // 登录密码
    private String PASSWORD;

    private int PLATFORM;

    public int getPLATFORM() {
        return PLATFORM;
    }

    public void setPLATFORM(int PLATFORM) {
        this.PLATFORM = PLATFORM;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public LoginReq(String USERNAME, String PASSWORD, int PLATFORM) {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.PLATFORM = PLATFORM;
    }
}
