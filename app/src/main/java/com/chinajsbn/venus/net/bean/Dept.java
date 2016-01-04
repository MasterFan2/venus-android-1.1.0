package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * Created by 13510 on 2015/10/22.
 */
@Table(name = "dept")
public class Dept extends BaseBean {

    @Column(column = "deptNo")
    private int    deptNo;

    @Column(column = "deptName")
    private String deptName;

    @Foreign(column = "deptNo", foreign = "id")
    private ArrayList<Emp> lists;

    public Dept(int deptNo, String deptName) {
        this.deptNo = deptNo;
        this.deptName = deptName;
    }

    public Dept(int deptNo, String deptName, ArrayList<Emp> lists) {
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.lists = lists;
    }

    public int getDeptNo() {

        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public ArrayList<Emp> getLists() {
        return lists;
    }

    public void setLists(ArrayList<Emp> lists) {
        this.lists = lists;
    }
}
