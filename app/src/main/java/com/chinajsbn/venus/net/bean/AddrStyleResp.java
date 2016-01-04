package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/9/23.
 */
public class AddrStyleResp {

    private AddrStyle data;
    private int code;

    public AddrStyle getData() {
        return data;
    }

    public void setData(AddrStyle data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AddrStyleResp(AddrStyle data, int code) {

        this.data = data;
        this.code = code;
    }
}
