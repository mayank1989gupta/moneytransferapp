package com.money.transfer.model;

/**
 * @author Mayank
 */
public class Error {

    private String msg;
    private String desc;

    public Error(String msg) {
        this.msg = msg;
    }

    public Error(String msg, String desc) {
        this.msg = msg;
        this.desc = desc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
