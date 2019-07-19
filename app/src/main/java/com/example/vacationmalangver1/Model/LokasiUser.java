package com.example.vacationmalangver1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class LokasiUser implements Parcelable {

    private GeoPoint geo_point;
    private @ServerTimestamp Date timestamp;
    private User user;

    public LokasiUser(GeoPoint geo_point, Date timestamp, User user) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
        this.user = user;
    }
    public LokasiUser() {

    }

    protected LokasiUser(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<LokasiUser> CREATOR = new Creator<LokasiUser>() {
        @Override
        public LokasiUser createFromParcel(Parcel in) {
            return new LokasiUser(in);
        }

        @Override
        public LokasiUser[] newArray(int size) {
            return new LokasiUser[size];
        }
    };

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LokasiUser{" +
                "geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                ", user=" + user +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
    }
}
