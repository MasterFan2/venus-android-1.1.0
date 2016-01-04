package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * Created by 13510 on 2015/11/30.
 */
public class BrandsResp {
    private List<Brands> data;
    private int code;

    public BrandsResp() {
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Brands> getData() {
        return data;
    }

    public void setData(List<Brands> data) {
        this.data = data;
    }

    public BrandsResp(int code, List<Brands> data) {

        this.code = code;
        this.data = data;
    }
}
