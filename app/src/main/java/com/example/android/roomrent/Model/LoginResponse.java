package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private String msg;

    @SerializedName("uses")
    @Expose
    private String uses;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("errors")
    @Expose
    private LoginErrors LoginErrors;
    @SerializedName("data")
    @Expose
    private LoginData loginData;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getUses() {
        return uses;
    }

    public User getUser() {
        return user;
    }

    public String getApiToken() {
        return apiToken;
    }

    public LoginResponse.LoginErrors getLoginErrors() {
        return LoginErrors;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", uses='" + uses + '\'' +
                ", user=" + user +
                ", apiToken='" + apiToken + '\'' +
                ", LoginErrors=" + LoginErrors +
                ", loginData=" + loginData +
                '}';
    }

    public class LoginErrors {

        @SerializedName("device_token")
        @Expose
        private List<String> deviceToken = null;
        @SerializedName("device_type")
        @Expose
        private List<String> deviceType = null;
        @SerializedName("identity")
        @Expose
        private List<String> identity = null;

        @SerializedName("password")
        @Expose

        private List<String> password = null;

        public List<String> getDeviceToken() {
            return deviceToken;
        }

        public List<String> getDeviceType() {
            return deviceType;
        }

        public List<String> getIdentity() {
            return identity;
        }

        public List<String> getPassword() {
            return password;
        }


        @Override
        public String toString() {
            return "LoginErrors{" +
                    "deviceToken=" + deviceToken +
                    ", deviceType=" + deviceType +
                    ", identity=" + identity +
                    ", password=" + password +
                    '}';
        }
    }

    private class LoginData {

        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("identity")
        @Expose
        private String identity;

        @Override
        public String toString() {
            return "LoginData{" +
                    "deviceType='" + deviceType + '\'' +
                    ", deviceToken='" + deviceToken + '\'' +
                    ", identity='" + identity + '\'' +
                    '}';
        }
    }
}