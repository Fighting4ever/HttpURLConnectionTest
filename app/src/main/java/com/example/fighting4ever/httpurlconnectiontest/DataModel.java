package com.example.fighting4ever.httpurlconnectiontest;

/**
 * Created by Fighting4Ever on 2016/4/20.
 */
public class DataModel {
    private String reason;
    private String result;
    private String error_code;
    private String resultcode;
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
