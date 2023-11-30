package com.project.angsurapp;

public class ObjectTrs {
    String nama, no_ktp, jenis_barang;

    public ObjectTrs(String nama, String no_ktp, String jenis_barang) {
        this.nama = nama;
        this.no_ktp = no_ktp;
        this.jenis_barang = jenis_barang;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public void setJenis_barang(String jenis_barang) {
        this.jenis_barang = jenis_barang;
    }

}
