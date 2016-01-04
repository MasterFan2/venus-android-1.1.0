package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * 样片/客片 列表
 * Created by MasterFan on 2015/6/18.
 * description:
 */
public class SimpleResp {
    private ArrayList<Simple> data;
    private int code;

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList<Simple> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SimpleResp() {

    }

    public SimpleResp(ArrayList<Simple> data, int code) {

        this.data = data;
        this.code = code;
    }
}
