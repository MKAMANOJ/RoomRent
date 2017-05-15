package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateAvatarResponse {

    @SerializedName("errors")
    @Expose
    private Errors errors;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("profile_image")
        @Expose
        private String profileImage;

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

    }

    public class Errors {

        @SerializedName("profile_image")
        @Expose
        private List<String> profileImage = null;

        public List<String> getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(List<String> profileImage) {
            this.profileImage = profileImage;
        }

    }

}