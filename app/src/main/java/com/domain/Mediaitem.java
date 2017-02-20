package com.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Mediaitem implements Parcelable {
    private String name;
    private long duration;
    private long size;
    private long id;
    private String data;

    public Mediaitem() {
    }

    protected Mediaitem(Parcel in) {
        name = in.readString();
        duration = in.readLong();
        size = in.readLong();
        id = in.readLong();
        data = in.readString();
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
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeLong(id);
        dest.writeString(data);
    }
}
