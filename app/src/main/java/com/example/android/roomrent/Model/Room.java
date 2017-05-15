package com.example.android.roomrent.Model;

public class Room {

    /**
     * Constant value that represents no image was provided for this place
     */
    private static final int NO_IMAGE_PROVIDED = -1;
    //Place where is room is available or needed
    private String place;
    private String noOfRoom;
    /**
     * Image resource ID for the room
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    //constructor when image is provided
    public Room(String place, String noOfRoom, int mImageResourceId) {
        this.place = place;
        this.noOfRoom = noOfRoom;
        this.mImageResourceId = mImageResourceId;
    }

    // constructor when no image is provided
    public Room(String place, String noOfRoom) {
        this.place = place;
        this.noOfRoom = noOfRoom;
    }

    //get the image resource Id
    public int getImageResourceId() {
        return mImageResourceId;
    }

    // check whether image resource id is provided or not
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public String getPlace() {
        return place;
    }

    public String getNoOfRoom() {
        return noOfRoom;
    }
}
