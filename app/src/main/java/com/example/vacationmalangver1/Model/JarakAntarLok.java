package com.example.vacationmalangver1.Model;

public class JarakAntarLok {
    int lokasiAwal;
    int lokasiTujuan;
    double jarak;
    String title;
    String id;

    public JarakAntarLok(String id, int lokasiAwal, int lokasiTujuan, double jarak, String title) {
        this.id = id;
        this.lokasiAwal = lokasiAwal;
        this.lokasiTujuan = lokasiTujuan;
        this.jarak = jarak;
        this.title = title;
    }

    public JarakAntarLok() {

    }

    public int getLokasiAwal() {
        return lokasiAwal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLokasiAwal(int lokasiAwal) {
        this.lokasiAwal = lokasiAwal;
    }

    public int getLokasiTujuan() {
        return lokasiTujuan;
    }

    public void setLokasiTujuan(int lokasiTujuan) {
        this.lokasiTujuan = lokasiTujuan;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
