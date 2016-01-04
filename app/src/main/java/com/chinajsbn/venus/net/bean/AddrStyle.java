package com.chinajsbn.venus.net.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MasterFan on 2015/7/2.
 * description:
 */
public class AddrStyle {

    private List<Address> address;
    private List<PhotoStyle>   style;

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<PhotoStyle> getStyle() {
        return style;
    }

    public void setStyle(List<PhotoStyle> style) {
        this.style = style;
    }

    public AddrStyle() {
    }

    public AddrStyle(List<Address> address, List<PhotoStyle> style) {

        this.address = address;
        this.style = style;
    }
}
