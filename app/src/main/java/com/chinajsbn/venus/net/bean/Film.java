package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/11/26.
 */
@Table(name = "Film")
public class Film {
    @Column(column = "coverImage")
    private CoverImage coverImage;

    @Column(column = "createDate")
    private String createDate;

    @Column(column = "hits")
    private int hits;

    @Column(column = "isUsed")
    private int isUsed;

    @Column(column = "name")
    private String name;

    @Column(column = "remark")
    private String remark;

    @Column(column = "seasonId")
    private int seasonId;

    @Column(column = "imgUrl")
    private String imgUrl;

    @Column(column = "url")
    private String url;

    @Id
    @Column(column = "videoId")
    private int videoId;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Column(column = "tag")
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public CoverImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public Film() {
    }

    public Film(CoverImage coverImage, String createDate, int hits, int isUsed, String name, String remark, int seasonId, String url, int videoId) {

        this.coverImage = coverImage;
        this.createDate = createDate;
        this.hits = hits;
        this.isUsed = isUsed;
        this.name = name;
        this.remark = remark;
        this.seasonId = seasonId;
        this.url = url;
        this.videoId = videoId;
    }
}
