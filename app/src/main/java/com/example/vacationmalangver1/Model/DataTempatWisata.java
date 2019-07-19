package com.example.vacationmalangver1.Model;

public class DataTempatWisata {

    int background;
    String namaTempat;
    String alamatTempat;


    public DataTempatWisata(){

    }

    public DataTempatWisata(int background, String namaTempat, String alamatTempat) {
        this.background = background;
        this.namaTempat = namaTempat;
        this.alamatTempat = alamatTempat;
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
}
