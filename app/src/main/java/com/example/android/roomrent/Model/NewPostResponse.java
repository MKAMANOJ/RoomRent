package com.example.android.roomrent.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewPostResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("post")
    @Expose
    private Post post;

    @SerializedName("errors")
    @Expose
    private Errors errors;

    public NewPostResponse(String code, String message, Post post, Errors errors) {
        this.code = code;
        this.message = message;
        this.post = post;
        this.errors = errors;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "NewPostResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", post=" + post +
                ", errors=" + errors +
                '}';
    }

    public class Errors {

        @SerializedName("title")
        @Expose
        public List<String> title = null;

        @SerializedName("description")
        @Expose
        public List<String> description = null;

        @SerializedName("no_of_rooms")
        @Expose
        public List<String> noOfRooms = null;

        @SerializedName("price")
        @Expose
        public List<String> price = null;

        @SerializedName("address")
        @Expose
        public List<String> address = null;

        @SerializedName("latitude")
        @Expose
        public List<String> latitude = null;

        @SerializedName("longitude")
        @Expose
        public List<String> longitude = null;

        @SerializedName("post_type")
        @Expose
        public List<String> postType = null;

        @SerializedName("images")
        @Expose
        public List<String> images = null;

        @SerializedName("images.1")
        @Expose
        public List<String> images1 = null;

        @SerializedName("images.2")
        @Expose
        public List<String> images2 = null;

        @SerializedName("images.3")
        @Expose
        public List<String> images3 = null;

        @SerializedName("images.4")
        @Expose
        public List<String> images4 = null;

        @SerializedName("images.5")
        @Expose
        public List<String> images5 = null;

        public Errors(List<String> title, List<String> description, List<String> noOfRooms,
                      List<String> price, List<String> address, List<String> latitude,
                      List<String> longitude, List<String> postType, List<String> images,
                      List<String> images1, List<String> images2, List<String> images3,
                      List<String> images4, List<String> images5) {
            this.title = title;
            this.description = description;
            this.noOfRooms = noOfRooms;
            this.price = price;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.postType = postType;
            this.images = images;
            this.images1 = images1;
            this.images2 = images2;
            this.images3 = images3;
            this.images4 = images4;
            this.images5 = images5;
        }

        @Override
        public String toString() {
            return "Errors{" +
                    "title=" + title +
                    ", description=" + description +
                    ", noOfRooms=" + noOfRooms +
                    ", price=" + price +
                    ", address=" + address +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", postType=" + postType +
                    ", images=" + images +
                    ", images1=" + images1 +
                    ", images2=" + images2 +
                    ", images3=" + images3 +
                    ", images4=" + images4 +
                    ", images5=" + images5 +
                    '}';
        }

        public List<String> getTitle() {
            return title;
        }

        public List<String> getDescription() {
            return description;
        }

        public List<String> getNoOfRooms() {
            return noOfRooms;
        }

        public List<String> getPrice() {
            return price;
        }

        public List<String> getAddress() {
            return address;
        }

        public List<String> getLatitude() {
            return latitude;
        }

        public List<String> getLongitude() {
            return longitude;
        }

        public List<String> getPostType() {
            return postType;
        }

        public List<String> getImages() {
            List<String> image = new ArrayList<String>();
            image.addAll(images);
            image.addAll(images1);
            image.addAll(images2);
            image.addAll(images3);
            image.addAll(images4);
            image.addAll(images5);
            return image;
        }
    }

    public class Post {

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
        @SerializedName("images")
        @Expose
        private List<String> images = null;


        public Post(Integer id, String title, String description,
                    Integer noOfRooms, Integer price, String address,
                    String latitude, String longitude, List<String> images) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.noOfRooms = noOfRooms;
            this.price = price;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
            this.images = images;
        }

        @Override
        public String toString() {
            return "NewPostResponse{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", noOfRooms=" + noOfRooms +
                    ", price=" + price +
                    ", address='" + address + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", images=" + images +
                    '}';
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getNoOfRooms() {
            return noOfRooms;
        }

        public void setNoOfRooms(Integer noOfRooms) {
            this.noOfRooms = noOfRooms;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

    }


}