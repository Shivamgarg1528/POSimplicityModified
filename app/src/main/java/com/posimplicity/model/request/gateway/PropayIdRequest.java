package com.posimplicity.model.request.gateway;

import com.google.gson.annotations.SerializedName;

public class PropayIdRequest {

    @SerializedName("Name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
