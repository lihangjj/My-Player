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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    private long duration;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(data);
        dest.writeLong(size);
    }

    public static final Parcelable.Creator<YinyueItem> CREATOR = new Parcelable.Creator<YinyueItem>() {
        @Override
        public YinyueItem createFromParcel(Parcel source) {
            YinyueItem yinyueItem = new YinyueItem();
            yinyueItem.name = source.readString();
            yinyueItem.data = source.readString();
            yinyueItem.size = source.readLong();


            return yinyueItem;
        }

        @Override
        public YinyueItem[] newArray(int size) {
            return new YinyueItem[size];
        }
    };
}
