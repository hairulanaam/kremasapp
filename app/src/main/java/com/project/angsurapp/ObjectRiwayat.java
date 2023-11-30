package com.project.angsurapp;

public class ObjectRiwayat {
    String nama, tanggal, jumlah, angsurke, barang;

    public ObjectRiwayat(String nama, String tanggal, String jumlah, String angsurke, String barang) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.angsurke = angsurke;
        this.barang = barang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getAngsurke() {
        return angsurke;
    }

    public void setAngsurke(String angsurke) {
        this.angsurke = angsurke;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }
}
