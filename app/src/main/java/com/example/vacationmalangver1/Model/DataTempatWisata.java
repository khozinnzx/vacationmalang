package com.example.vacationmalangver1.Model;

public class DataTempatWisata {

    int background;
    String namaTempat;
    String alamatTempat;
    String hargaTiket;
    String deskripsi;


    public DataTempatWisata(){

    }

    public DataTempatWisata(int background, String namaTempat, String alamatTempat, String deskripsi, String hargaTiket) {
        this.background = background;
        this.namaTempat = namaTempat;
        this.alamatTempat = alamatTempat;
        this.deskripsi = deskripsi;
        this.hargaTiket = hargaTiket;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getAlamatTempat() {
        return alamatTempat;
    }

    public void setAlamatTempat(String alamatTempat) {
        this.alamatTempat = alamatTempat;
    }

    public String getHargaTiket() {
        return hargaTiket;
    }

    public void setHargaTiket(String hargaTiket) {
        this.hargaTiket = hargaTiket;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
