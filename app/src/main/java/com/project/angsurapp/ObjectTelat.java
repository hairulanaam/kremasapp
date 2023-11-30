package com.project.angsurapp;

public class ObjectTelat {
    String nama_pelanggan, tgl_jatuhtempo, denda;

    public ObjectTelat(String nama_pelanggan, String tgl_jatuhtempo, String denda) {
        this.nama_pelanggan = nama_pelanggan;
        this.tgl_jatuhtempo = tgl_jatuhtempo;
        this.denda = denda;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getTgl_jatuhtempo() {
        return tgl_jatuhtempo;
    }

    public void setTgl_jatuhtempo(String tgl_jatuhtempo) {
        this.tgl_jatuhtempo = tgl_jatuhtempo;
    }

    public String getDenda() {
        return denda;
    }

    public void setDenda(String denda) {
        this.denda = denda;
    }
}
