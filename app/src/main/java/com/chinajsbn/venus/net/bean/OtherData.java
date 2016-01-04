package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * Created by 13510 on 2015/11/30.
 */
public class OtherData {

    private int totalCount;
    private List<Other> data;

    public OtherData() {
    }

    public List<Other> getData() {

        return data;
    }

    public void setData(List<Other> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public OtherData(List<Other> data, int totalCount) {

        this.data = data;
        this.totalCount = totalCount;
    }
}
