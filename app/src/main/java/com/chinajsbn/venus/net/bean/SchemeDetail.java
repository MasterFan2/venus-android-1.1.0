package com.chinajsbn.venus.net.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 13510 on 2015/9/7.
 */
public class SchemeDetail implements Serializable{
    private String schemeDesc;
    private String schemeName;
    private ArrayList<ImageItem> imageList;
    private ArrayList<SchemeStyle> schemeStyles;

    public String getSchemeDesc() {
        return schemeDesc;
    }

    public void setSchemeDesc(String schemeDesc) {
        this.schemeDesc = schemeDesc;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public ArrayList<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<SchemeStyle> getSchemeStyles() {
        return schemeStyles;
    }

    public void setSchemeStyles(ArrayList<SchemeStyle> schemeStyles) {
        this.schemeStyles = schemeStyles;
    }

    public SchemeDetail(String schemeDesc, String schemeName, ArrayList<ImageItem> imageList, ArrayList<SchemeStyle> schemeStyles) {

        this.schemeDesc = schemeDesc;
        this.schemeName = schemeName;
        this.imageList = imageList;
        this.schemeStyles = schemeStyles;
    }
}
