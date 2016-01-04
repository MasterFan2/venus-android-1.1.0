package com.chinajsbn.venus.net.bean;

/**
 * Created by MasterFan on 2015/4/25.
 * description:
 */
public class BaseResp {
    private int code;
    private boolean success;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BaseResp(int code, boolean success, String message, String data) {

        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                ", code=" + code +
                '}';
    }
}
