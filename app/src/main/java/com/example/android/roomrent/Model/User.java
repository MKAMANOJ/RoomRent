package com.example.android.roomrent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    public User(int id, String name, String email, String username, String phone, String createdAt, String profileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
    }

    private User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        username = in.readString();
        phone = in.readString();
        createdAt = in.readString();
        profileImage = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone=" + phone +
                ", createdAt='" + createdAt + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(createdAt);
        dest.writeString(profileImage);
    }
}