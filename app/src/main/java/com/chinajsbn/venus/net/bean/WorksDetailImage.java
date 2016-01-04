package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/9/23.
 */
@Table(name = "WorksDetailImage")
public class WorksDetailImage {

    @Id
    @Column(column = "contentId")
    private String contentId;

    @Column(column = "imageName")
    private String imageName;

    @Column(column = "imageTypeId")
    private int imageTypeId;

    @Column(column = "imageUrl")
    private String imageUrl;

    @Column(column = "mimeTypeId")
    private String mimeTypeId;

    @Column(column = "uniqueTag")
    private String uniqueTag;

    @Column(column = "weight")
    private int weight;

    public String getUniqueTag() {
        return uniqueTag;
    }

    public void setUniqueTag(String uniqueTag) {
        this.uniqueTag = uniqueTag;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageTypeId() {
        return imageTypeId;
    }

    public void setImageTypeId(int imageTypeId) {
        this.imageTypeId = imageTypeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMimeTypeId() {
        return mimeTypeId;
    }

    public void setMimeTypeId(String mimeTypeId) {
        this.mimeTypeId = mimeTypeId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public WorksDetailImage(){}

    public WorksDetailImage(String contentId, String imageName, int imageTypeId, String imageUrl, String mimeTypeId, int weight) {

        this.contentId = contentId;
        this.imageName = imageName;
        this.imageTypeId = imageTypeId;
        this.imageUrl = imageUrl;
        this.mimeTypeId = mimeTypeId;
        this.weight = weight;
    }
}
