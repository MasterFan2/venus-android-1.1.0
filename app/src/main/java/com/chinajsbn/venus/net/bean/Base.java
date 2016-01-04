package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/6/25.
 * description:
 */
public class Base<T> {
    private int code;
    private int totalCount;
    private T data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Base() {

    }

    public Base(int code, T data) {

        this.code = code;
        this.data = data;
    }
}
