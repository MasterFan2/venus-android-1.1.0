package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * 首页数据
 * Created by MasterFan on 2015/6/18.
 * description:
 */
public class HomeMenuResp extends Base {

    private int code;

    private List<HomeMenu> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<HomeMenu> getData() {
        return data;
    }

    public void setData(List<HomeMenu> data) {
        this.data = data;
    }

    public HomeMenuResp(int code, List<HomeMenu> data) {

        this.code = code;
        this.data = data;
    }
}
