package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ForgotPasswordRequest {

    @SerializedName("email")
    @Expose
    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }
}
