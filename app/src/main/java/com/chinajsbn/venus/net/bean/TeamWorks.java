package com.chinajsbn.venus.net.bean;

import java.util.List;

/**
 * Created by 13510 on 2015/12/2.
 */
public class TeamWorks {

    List<DetailSubImg> detailedImages;

    public List<DetailSubImg> getDetailedImages() {
        return detailedImages;
    }

    public void setDetailedImages(List<DetailSubImg> detailedImages) {
        this.detailedImages = detailedImages;
    }

    public TeamWorks(List<DetailSubImg> detailedImages) {

        this.detailedImages = detailedImages;
    }
}
