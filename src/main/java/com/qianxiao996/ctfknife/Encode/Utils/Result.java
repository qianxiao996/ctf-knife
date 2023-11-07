package com.qianxiao996.ctfknife.Encode.Utils;

public class Result {
    private String name;
    private Boolean is_success;
    private String result_text;

    public Result(String name, Boolean is_success, String result_text) {
        this.name = name;
        this.is_success = is_success;
        this.result_text = result_text;
    }

    public Boolean is_success() {
        return is_success;
    }

    public String getName() {
        return name;
    }
    public String getResult_text() {
        return result_text;
    }
}