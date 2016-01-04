package com.chinajsbn.venus.net.bean;

/**
 * 酒店搜索条件
 * Created by 13510 on 2015/12/25.
 */
public class SearchHotelParam {
    public int minTable = -1;//default -1 也就是没有选过
    public int maxTable = -1;
    public int minPrice = -1;
    public int maxPrice = -1;
    public int isGift   = -1;
    public int isDisaccount = -1;
    public String sort  = "price"; //price table
    public String order = "desc";   // asc desc
    public int cityId   = -1;

    public int pageSize  = 10;
    public int pageIndex = 1;

    ///重设所有
    public void resetAll(){
        minTable = -1;//default -1 也就是没有选过
        maxTable = -1;
        minPrice = -1;
        maxPrice = -1;
        isGift   = -1;
        isDisaccount = -1;
        sort  = "price";
        order = "desc";
        cityId = -1;

        pageSize  = 10;
        pageIndex = 1;
    }

    ///重设筛选条件
    public void resetFilter(){
        minTable = -1;//default -1 也就是没有选过
        maxTable = -1;
        minPrice = -1;
        maxPrice = -1;
        isGift   = -1;
        isDisaccount = -1;
        cityId = -1;
    }
}
