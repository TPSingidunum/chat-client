package com.masofino.birp.chatclient.api;

import com.google.gson.annotations.SerializedName;

// Registration request model
public class RegistrationRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("certificate")
    private String certificate;

    public RegistrationRequest(String username, String email, String certificate) {
        this.username = username;
        this.email = email;
        this.certificate = certificate;
    }
}