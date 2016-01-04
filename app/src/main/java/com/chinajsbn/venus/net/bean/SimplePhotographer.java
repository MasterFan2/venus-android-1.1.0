package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by 13510 on 2015/8/29.
 */
@Table(name = "SimplePhotographer")
public class SimplePhotographer implements Serializable {

    @Column(column = "empNo")
    private String empNo;

    @Id
    @Column(column = "personId")
    private String personId;

    @Column(column = "personName")
    private String personName;

    @Column(column = "photoUrl")
    private String photoUrl;

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public SimplePhotographer(String empNo, String personId, String personName, String photoUrl) {

        this.empNo = empNo;
        this.personId = personId;
        this.personName = personName;
        this.photoUrl = photoUrl;
    }

    public SimplePhotographer() {}
}
