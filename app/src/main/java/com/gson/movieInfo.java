package com.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MovieInfo implements Parcelable {
    protected MovieInfo(Parcel in) {
        movieName = in.readString();
        movieUrl = in.readString();
        Imageurl = in.readString();
        movieId = in.readString();
        movieshuoming = in.readString();
        videoLength = in.readString();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public String getMovieshuoming() {
        return movieshuoming;
    }

    public String movieName;
    @SerializedName("hightUrl")
    public String movieUrl;

    public String getImageurl() {
        return Imageurl;
    }

    @SerializedName("coverImg")
    public String Imageurl;
    public String movieId;
    @SerializedName("summary")
    public String movieshuoming;

    public String getVideoLength() {
        return videoLength;
    }

    private String videoLength;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(movieUrl);
        dest.writeString(Imageurl);
        dest.writeString(movieId);
        dest.writeString(movieshuoming);
        dest.writeString(videoLength);
    }
}
