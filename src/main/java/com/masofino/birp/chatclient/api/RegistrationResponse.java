package com.masofino.birp.chatclient.api;

import com.google.gson.annotations.SerializedName;

// Registration response model
public class RegistrationResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private UserData user;

    public String getMessage() {
        return message;
    }

    public UserData getUser() {
        return user;
    }

    public boolean isSuccess() {
        return user != null;
    }

    public static class UserData {
        @SerializedName("userId")
        private int userId;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("certificate")
        private String certificate;

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getCertificate() {
            return certificate;
        }
    }
}