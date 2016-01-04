package com.chinajsbn.venus.net.bean;

import java.io.Serializable;

/**
 * 团队造型师/摄影师
 * Created by 13510 on 2015/12/2.
 */
public class PhotoStylistDetail implements Serializable {

    private String head;
    private int personId;
    private String personName;

    public PhotoStylistDetail() {}

    public String getHead() {

        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public PhotoStylistDetail(String head, int personId, String personName) {

        this.head = head;
        this.personId = personId;
        this.personName = personName;
    }
}
