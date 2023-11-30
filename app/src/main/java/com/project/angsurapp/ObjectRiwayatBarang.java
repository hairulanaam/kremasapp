package com.project.angsurapp;

public class ObjectRiwayatBarang {
    String nama_barang, jenis_barang, harga_jual;

    public ObjectRiwayatBarang(String nama_barang, String jenis_barang, String harga_jual){
        this.nama_barang = nama_barang;
        this.jenis_barang = jenis_barang;
        this.harga_jual = harga_jual;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public void setJenis_barang(String jenis_barang) {
        this.jenis_barang = jenis_barang;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }
}
