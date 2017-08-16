package com.posimplicity.model.response.other;

import com.google.gson.annotations.SerializedName;

public class Validate {

    @SerializedName("success")
    private int success;

    @SerializedName("msg")
    private String msg;

    public int getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
