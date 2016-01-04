package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by MasterFan on 2015/7/2.
 * description:
 */
@Table(name = "Address")
public class Address {

    @Id
    @Column(column = "addressId")
    private String addressId;

    @Column(column = "addressName")
    private String addressName;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Address() {

    }

    public Address(String addressName, String addressId) {

        this.addressName = addressName;
        this.addressId = addressId;
    }
}
