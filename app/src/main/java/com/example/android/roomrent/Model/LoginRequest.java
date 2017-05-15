package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("device_type")
    @Expose
    private String deviceType;

    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("identity")

    @Expose
    private String identity;
    @SerializedName("password")
    @Expose
    private String password;

    public LoginRequest(String deviceToken, String type, String identity, String password) {
        this.deviceToken = deviceToken;
        this.identity = identity;
        this.password = password;
        this.deviceType = "2";
    }
}
