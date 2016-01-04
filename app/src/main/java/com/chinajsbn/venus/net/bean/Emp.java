package com.chinajsbn.venus.net.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 13510 on 2015/10/22.
 */
@Table(name = "Emp")
public class Emp extends BaseBean{

    @Column(column = "empNo")
    private int    empNo;

    @Column(column = "empName")
    private String empName;

    @Column(column = "nickName")
    private String nickName;

//    @Foreign(column = "deptNo", foreign = "id")
//    private Dept dept;

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Emp(int empNo, String empName, String nickName) {
        this.empNo = empNo;
        this.empName = empName;
        this.nickName = nickName;
    }

    //    public Dept getDept() {
//        return dept;
//    }
//
//    public void setDept(Dept dept) {
//        this.dept = dept;
//    }
//
//    public Emp(int empNo, String empName, String nickName, Dept dept) {
//
//        this.empNo = empNo;
//        this.empName = empName;
//        this.nickName = nickName;
//        this.dept = dept;
//    }
}
