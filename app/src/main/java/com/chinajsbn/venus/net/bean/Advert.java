package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/10/15.
 */
@Table(name = "Advert")
public class Advert {

    @Id
    @Column(column = "contentId")
    private String contentId;

    @Column(column = "contentUrl")
    private String contentUrl;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    public Advert(){}
    public Advert(String contentId, String contentUrl) {

        this.contentId = contentId;
        this.contentUrl = contentUrl;
    }
}
