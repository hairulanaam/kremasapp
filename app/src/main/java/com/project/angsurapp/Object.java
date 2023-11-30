package com.project.angsurapp;

public class Object {

    String nama_barang, harga, stok, deskripsi;
//    byte[] gambar_barang;

    public Object(String nama_barang, String harga, String stok, String deskripsi) {
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.stok = stok;
        this.deskripsi = deskripsi;
//        this.gambar_barang = gambar_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

//    public byte[] getGambar_barang() {
//        return gambar_barang;
//    }
//
//    public void setGambar_barang(byte[] gambar_barang) {
//        this.gambar_barang = gambar_barang;
//    }
}
