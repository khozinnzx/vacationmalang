package com.example.vacationmalangver1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class LokasiWisata implements Parcelable {

    public String nama_wisata;
    public GeoPoint geo_point_wisata;
    public int id_wisata;

    public LokasiWisata(GeoPoint geo_point_wisata, String nama_wisata, int id_wisata) {
        this.nama_wisata = nama_wisata;
        this.geo_point_wisata = geo_point_wisata;
        this.id_wisata = id_wisata;
    }

    public LokasiWisata() {

    }

    protected LokasiWisata(Parcel in) {
        nama_wisata = in.readString();
    }

    public static final Creator<LokasiWisata> CREATOR = new Creator<LokasiWisata>() {
        @Override
        public LokasiWisata createFromParcel(Parcel in) {
            return new LokasiWisata(in);
        }

        @Override
        public LokasiWisata[] newArray(int size) {
            return new LokasiWisata[size];
        }
    };

    public String getNama_wisata() {
        return nama_wisata;
    }

    public void setNama_wisata(String nama_wisata) {
        this.nama_wisata = nama_wisata;
    }

    public GeoPoint getGeo_point_wisata() {
        return geo_point_wisata;
    }

    public void setGeo_point_wisata(GeoPoint geo_point_wisata) {
        this.geo_point_wisata = geo_point_wisata;
    }

    public int getId_wisata() {
        return id_wisata;
    }

    public void setId_wisata(int id_wisata) {
        this.id_wisata = id_wisata;
    }


    @Override
    public String toString() {
        return "LokasiWisata{" +
                "nama_wisata='" + nama_wisata + '\'' +
                ", geo_point_wisata='" + geo_point_wisata + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama_wisata);
    }
}
