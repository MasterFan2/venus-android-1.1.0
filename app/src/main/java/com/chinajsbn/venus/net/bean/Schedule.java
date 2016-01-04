package com.chinajsbn.venus.net.bean;

/**
 * Created by master on 15-8-1.
 * description:人员档期
 */
public class Schedule {
    private int    scheduleId;
    private String scheduleDate;//锁定日期
    private int    orderId;     //订单编号（即那天被哪个订单给锁定）
    private int    statusId;
    private String remark;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Schedule(int scheduleId, String scheduleDate, int orderId, int statusId, String remark) {

        this.scheduleId = scheduleId;
        this.scheduleDate = scheduleDate;
        this.orderId = orderId;
        this.statusId = statusId;
        this.remark = remark;
    }
}
