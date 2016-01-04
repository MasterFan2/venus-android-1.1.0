package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;

/**
 * Created by MasterFan on 2015/6/24.
 * description:
 */
public class MenuResp {

    private ArrayList<Submenu> data;
    private int code;

    public ArrayList<Submenu> getData() {
        return data;
    }

    public void setData(ArrayList<Submenu> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MenuResp(ArrayList<Submenu> data, int code) {

        this.data = data;
        this.code = code;
    }
}
