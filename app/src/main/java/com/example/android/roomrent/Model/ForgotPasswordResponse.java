package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private String message;

    public ForgotPasswordResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ForgotPasswordResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
