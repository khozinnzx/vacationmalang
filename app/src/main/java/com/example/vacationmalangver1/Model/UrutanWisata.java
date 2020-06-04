package com.example.vacationmalangver1.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UrutanWisata implements Parcelable {
    String namaWisata;

    public UrutanWisata(String namaWisata) {
        this.namaWisata = namaWisata;
    }

    public UrutanWisata() {

    }

    protected UrutanWisata(Parcel in) {
        namaWisata = in.readString();
    }

    public static final Creator<UrutanWisata> CREATOR = new Creator<UrutanWisata>() {
        @Override
        public UrutanWisata createFromParcel(Parcel in) {
            return new UrutanWisata(in);
        }

        @Override
        public UrutanWisata[] newArray(int size) {
            return new UrutanWisata[size];
        }
    };

    public String getNamaWisata() {
        return namaWisata;
    }

    public void setNamaWisata(String namaWisata) {
        this.namaWisata = namaWisata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(namaWisata);
    }
}
