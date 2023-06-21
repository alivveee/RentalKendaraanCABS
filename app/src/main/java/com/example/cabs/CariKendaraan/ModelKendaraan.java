package com.example.cabs.CariKendaraan;

public class ModelKendaraan {
    private String namaKendaraan, tahunKendaraan, tarifKendaraan, jenisMesin
            , jumlahPenumpang, jumlahKendaraan, deskripsi, urlGambar;
    private String key;

    public ModelKendaraan(){

    }

    public ModelKendaraan(String namaKendaraan, String tahunKendaraan, String tarifKendaraan, String jenisMesin, String jumlahPenumpang, String jumlahKendaraan, String deskripsi, String urlGambar) {
        this.namaKendaraan = namaKendaraan;
        this.tahunKendaraan = tahunKendaraan;
        this.tarifKendaraan = tarifKendaraan;
        this.jenisMesin = jenisMesin;
        this.jumlahPenumpang = jumlahPenumpang;
        this.jumlahKendaraan = jumlahKendaraan;
        this.deskripsi = deskripsi;
        this.urlGambar = urlGambar;
    }

    public String getNamaKendaraan() {
        return namaKendaraan;
    }

    public void setNamaKendaraan(String namaKendaraan) {
        this.namaKendaraan = namaKendaraan;
    }

    public String getTahunKendaraan() {
        return tahunKendaraan;
    }

    public void setTahunKendaraan(String tahunKendaraan) {
        this.tahunKendaraan = tahunKendaraan;
    }

    public String getTarifKendaraan() {
        return tarifKendaraan;
    }

    public void setTarifKendaraan(String tarifKendaraan) {
        this.tarifKendaraan = tarifKendaraan;
    }

    public String getJenisMesin() {
        return jenisMesin;
    }

    public void setJenisMesin(String jenisMesin) {
        this.jenisMesin = jenisMesin;
    }

    public String getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public void setJumlahPenumpang(String jumlahPenumpang) {
        this.jumlahPenumpang = jumlahPenumpang;
    }

    public String getJumlahKendaraan() {
        return jumlahKendaraan;
    }

    public void setJumlahKendaraan(String jumlahKendaraan) {
        this.jumlahKendaraan = jumlahKendaraan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }
}
