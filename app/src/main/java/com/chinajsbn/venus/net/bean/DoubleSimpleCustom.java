package com.chinajsbn.venus.net.bean;

/**
 * Created by 13510 on 2015/9/22.
 */
public class DoubleSimpleCustom {
    private Simple data1;
    private Simple data2;

    public Simple getData1() {
        return data1;
    }

    public void setData1(Simple data1) {
        this.data1 = data1;
    }

    public Simple getData2() {
        return data2;
    }

    public void setData2(Simple data2) {
        this.data2 = data2;
    }

    public DoubleSimpleCustom(Simple data1, Simple data2) {

        this.data1 = data1;
        this.data2 = data2;
    }
}
