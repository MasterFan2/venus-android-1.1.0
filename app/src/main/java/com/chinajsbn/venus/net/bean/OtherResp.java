package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/11/30.
 */
public class OtherResp {
    private OtherData data;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public OtherData getData() {
        return data;
    }

    public void setData(OtherData data) {
        this.data = data;
    }

    public OtherResp() {

    }

    public OtherResp(int code, OtherData data) {

        this.code = code;
        this.data = data;
    }
}
