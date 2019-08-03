package com.eservice.iot.core;

/**
 * @author zt
 */
public class ResponseModel {
    private String result;
    private String data;
    private int rtn;
    private int code;
    private String message;

    private int total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRtn() {
        return rtn;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRtn(int rtn) {
        this.rtn = rtn;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
