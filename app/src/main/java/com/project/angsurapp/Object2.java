package com.project.angsurapp;

public class Object2 {
    String nama_pelanggan, no_hp, alamat, sisa_angsuran;
    public Object2(String nama_pelanggan, String no_hp, String alamat, String sisa_angsuran) {
        this.nama_pelanggan = nama_pelanggan;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.sisa_angsuran = sisa_angsuran;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getSisa_angsuran() {
        return sisa_angsuran;
    }

    public void setSisa_angsuran(String sisa_angsuran) {
        this.sisa_angsuran = sisa_angsuran;
    }
}
