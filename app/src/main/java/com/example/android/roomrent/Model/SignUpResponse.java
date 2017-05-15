package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignUpResponse {

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    @SerializedName("data")
    @Expose
    private Data data;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "SignUpResponse{" +
                "user=" + user +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", data=" + data +
                '}';
    }

    public class Errors {

        @SerializedName("email")
        @Expose
        public List<String> email = null;

        @SerializedName("name")
        @Expose
        public List<String> name = null;

        @SerializedName("password")
        @Expose
        public List<String> password = null;
        @SerializedName("phone")

        @Expose
        public List<String> phone = null;
        @SerializedName("username")
        @Expose
        public List<String> username = null;

        public List<String> getEmail() {
            return email;
        }

        public List<String> getName() {
            return name;
        }

        public List<String> getPassword() {
            return password;
        }

        public List<String> getPhone() {
            return phone;
        }

        public List<String> getUsername() {
            return username;
        }

        @Override
        public String toString() {
            return "Errors{" +
                    "email=" + email +
                    ", name=" + name +
                    ", password=" + password +
                    ", phone=" + phone +
                    ", username=" + username +
                    '}';
        }
    }

    private class Data {

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("phone")
        @Expose
        private String phone;

        @Override
        public String toString() {
            return "Data{" +
                    "name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

}