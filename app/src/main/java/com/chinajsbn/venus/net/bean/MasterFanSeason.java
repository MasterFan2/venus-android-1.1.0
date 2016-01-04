package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/12/3.
 */
@Table(name = "MasterFanSeason")
public class MasterFanSeason {

    @Id
    @Column(column = "seasonId")
    private String seasonId;

    @Column(column = "coverUrl")
    private String coverUrl;

    @Column(column = "mobileUrl")
    private String mobileUrl;

    @Column(column = "seasonName")
    private String seasonName;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public MasterFanSeason(){}

    public MasterFanSeason(String coverUrl, String mobileUrl, String seasonId, String seasonName) {

        this.coverUrl = coverUrl;
        this.mobileUrl = mobileUrl;
        this.seasonId = seasonId;
        this.seasonName = seasonName;
    }
}
