package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/7/23.
 * description:
 */
public class VersionResp {
    private VersionInfo data;
    private int code;

    public VersionInfo getData() {
        return data;
    }

    public void setData(VersionInfo data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public VersionResp(VersionInfo data, int code) {

        this.data = data;
        this.code = code;
    }
}
