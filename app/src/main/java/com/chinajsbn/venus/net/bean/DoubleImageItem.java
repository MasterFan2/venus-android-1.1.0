package com.chinajsbn.venus.net.bean;

/**
 * 案例详情显示单 双
 * Created by 13510 on 2015/12/8.
 */
public class DoubleImageItem {
    private ImageItem data1;
    private ImageItem data2;

    public ImageItem getData1() {
        return data1;
    }

    public void setData1(ImageItem data1) {
        this.data1 = data1;
    }

    public ImageItem getData2() {
        return data2;
    }

    public void setData2(ImageItem data2) {
        this.data2 = data2;
    }

    public DoubleImageItem(ImageItem data1, ImageItem data2) {

        this.data1 = data1;
        this.data2 = data2;
    }
}
