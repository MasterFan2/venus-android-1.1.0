package com.chinajsbn.venus.net.bean;

/**
 * Created by chenjianjun on 15-4-14.
 * <p/>
 * Beyond their own, let the future
 */
public class LoginResp {

    // 结果 成功是true 失败是false
    private int code;

    public LoginResp() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LoginResp(int code) {

        this.code = code;
    }
}
