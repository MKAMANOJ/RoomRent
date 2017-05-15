package com.example.android.roomrent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostDatum implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("no_of_rooms")
    @Expose
    private Integer noOfRooms;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("post_type")
    @Expose
    private Integer postType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("user")
    @Expose
    private User user;

    public PostDatum() {
    }

    // When Images were Provided i.e. case of offers
    public PostDatum(Integer id, String title, String description, Integer noOfRooms, Integer price,
                     String address, String latitude, String longitude, Integer postType,
                     String createdAt, List<String> images, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.noOfRooms = noOfRooms;
        this.price = price;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postType = postType;
        this.createdAt = createdAt;
        this.images = images;
        this.user = user;
    }

    // When No Image was Provided i.e. case of ask
    public PostDatum(Integer id, String title, String description, Integer noOfRooms, Integer price,
                     String address, String latitude, String longitude, Integer postType,
                     String createdAt, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.noOfRooms = noOfRooms;
        this.price = price;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postType = postType;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String toString() {
        return "PostDatum{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", noOfRooms=" + noOfRooms +
                ", price=" + price +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", postType=" + postType +
                ", createdAt='" + createdAt + '\'' +
                ", images=" + images +
                ", user=" + user +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNoOfRooms() {
        return noOfRooms;
    }

    public Integer getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Integer getPostType() {
        return postType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<String> getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public static Creator<PostDatum> getCREATOR() {
        return CREATOR;
    }

    protected PostDatum(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        title = in.readString();
        description = in.readString();
        noOfRooms = in.readByte() == 0x00 ? null : in.readInt();
        price = in.readByte() == 0x00 ? null : in.readInt();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        postType = in.readByte() == 0x00 ? null : in.readInt();
        createdAt = in.readString();
        if (in.readByte() == 0x01) {
            images = new ArrayList<String>();
            in.readList(images, String.class.getClassLoader());
        } else {
            images = null;
        }
        user = (User) in.readValue(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(description);
        if (noOfRooms == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(noOfRooms);
        }
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(price);
        }
        dest.writeString(address);
        dest.writeString(latitude);
        dest.writeString(longitude);
        if (postType == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(postType);
        }
        dest.writeString(createdAt);
        if (images == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
        dest.writeValue(user);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PostDatum> CREATOR = new Parcelable.Creator<PostDatum>() {
        @Override
        public PostDatum createFromParcel(Parcel in) {
            return new PostDatum(in);
        }

        @Override
        public PostDatum[] newArray(int size) {
            return new PostDatum[size];
        }
    };
}