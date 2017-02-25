package com.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/23.
 */
//代表一个视频或音频
public class YinyueItem implements Parcelable {
    private String name;
    private long size;
    private String artist;
    private long duration;
    private String data;
    private boolean isLike;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public YinyueItem() {
    }

    protected YinyueItem(Parcel in) {
        name = in.readString();
        size = in.readLong();
        artist = in.readString();
        duration = in.readLong();
        data = in.readString();
        isLike = in.readByte() != 0;
    }

    public static final Creator<YinyueItem> CREATOR = new Creator<YinyueItem>() {
        @Override
        public YinyueItem createFromParcel(Parcel in) {
            return new YinyueItem(in);
        }

        @Override
        public YinyueItem[] newArray(int size) {
            return new YinyueItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeString(data);
        dest.writeByte((byte) (isLike ? 1 : 0));
    }
}
