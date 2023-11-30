package com.project.angsurapp;

public class PaymentItem {
    private String tglBayar;
    private String jumlahBayar;
    private String angsuranKe;
    private String denda_angsur;

    public PaymentItem(String tglBayar, String jumlahBayar, String angsuranKe, String denda_angsur) {
        this.tglBayar = tglBayar;
        this.jumlahBayar = jumlahBayar;
        this.angsuranKe = angsuranKe;
        this.denda_angsur = denda_angsur;
    }

    public String getTglBayar() {
        return tglBayar;
    }

    public String getJumlahBayar() {
        return jumlahBayar;
    }

    public String getAngsuranKe() {
        return angsuranKe;
    }

    public String getDenda_angsur() {
        return denda_angsur;
    }
}

