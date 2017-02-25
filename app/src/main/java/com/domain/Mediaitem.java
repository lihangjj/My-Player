package com.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Mediaitem implements Parcelable {
    private String name;
    private long duration;
    private long size;
    private long id;
    private String videoLength;
    private String data;
    private String imageUrl;
    private String shipinshuoming;

    public Mediaitem() {

    }

    protected Mediaitem(Parcel in) {
        name = in.readString();
        duration = in.readLong();
        size = in.readLong();
        id = in.readLong();
        videoLength = in.readString();
        data = in.readString();
        imageUrl = in.readString();
        shipinshuoming = in.readString();
    }

    public static final Creator<Mediaitem> CREATOR = new Creator<Mediaitem>() {
        @Override
        public Mediaitem createFromParcel(Parcel in) {
            return new Mediaitem(in);
        }

        @Override
        public Mediaitem[] newArray(int size) {
            return new Mediaitem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShipinshuoming() {
        return shipinshuoming;
    }

    public void setShipinshuoming(String shipinshuoming) {
        this.shipinshuoming = shipinshuoming;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeLong(id);
        dest.writeString(videoLength);
        dest.writeString(data);
        dest.writeString(imageUrl);
        dest.writeString(shipinshuoming);
    }
}
