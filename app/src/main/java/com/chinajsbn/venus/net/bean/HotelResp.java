package com.chinajsbn.venus.net.bean;

/**
 * 酒店列表
 * <p/>
 * Created by MasterFan on 2015/6/23.
 * description:
 */
public class HotelResp {

    private int hotelId;
    private String hotelName;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public HotelResp(int hotelId, String hotelName) {

        this.hotelId = hotelId;
        this.hotelName = hotelName;
    }
}
