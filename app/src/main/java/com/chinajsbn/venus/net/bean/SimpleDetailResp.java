package com.chinajsbn.venus.net.bean;

/**
 * 样片/客片 详情返回
 *
 * Created by MasterFan on 2015/6/18.
 * description:
 */
public class SimpleDetailResp {

    private SimpleDetail data;
    private int code;

    public SimpleDetail getData() {
        return data;
    }

    public void setData(SimpleDetail data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SimpleDetailResp() {

    }

    public SimpleDetailResp(SimpleDetail data, int code) {

        this.data = data;
        this.code = code;
    }
}
