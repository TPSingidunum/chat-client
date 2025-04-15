package com.masofino.birp.chatclient.api;

import com.google.gson.annotations.SerializedName;

public class OtpResponse {
    @SerializedName("token")
    private String token;

    public OtpResponse() {}

    public String getToken() {
        return token;
    }
}