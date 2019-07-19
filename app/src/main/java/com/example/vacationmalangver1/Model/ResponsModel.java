package com.example.vacationmalangver1.Model;

import java.util.List;

public class ResponsModel {

    String kode, pesan;
    List<DataTempatWisata> result;

    public List<DataTempatWisata> getResult() {
        return result;
    }

    public void setResult(List<DataTempatWisata> result) {
        this.result = result;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
