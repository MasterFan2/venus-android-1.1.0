package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * Created by 13510 on 2015/11/26.
 */
public class FilmResp {

    private int totalCount;
    private List<Film> data;

    public List<Film> getData() {
        return data;
    }

    public void setData(List<Film> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public FilmResp(List<Film> data, int totalCount) {

        this.data = data;
        this.totalCount = totalCount;
    }
}
