package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SignUpRequest {

    @SerializedName("profile_image")
    @Expose
    private File profilePicture;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;

    public SignUpRequest(File profilePicture, String name, String username, String email, String password, String phone) {
        this.profilePicture = profilePicture;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    public SignUpRequest( String name, String username, String email, String password, String phone) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}