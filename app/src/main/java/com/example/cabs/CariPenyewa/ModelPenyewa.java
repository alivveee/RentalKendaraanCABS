package com.example.cabs.CariPenyewa;

public class ModelPenyewa {

    private String nama;
    private String no_hp;
    private String tanggal;
    private String nama_kendaraan;
    private String harga;
    private String image;
    private String key;

    private String alamat;


//    String nama_kendaraan, String harga

    public ModelPenyewa(String nama, String no_hp,String alamat, String tanggal) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.no_hp = no_hp;
        this.alamat = alamat;

    }

    public ModelPenyewa() {
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNama_kendaraan() {
        return nama_kendaraan;
    }

    public String getHarga() {
        return harga;
    }


    public String getImage() {
        return image;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public String getAlamat() {
        return alamat;
    }
}

