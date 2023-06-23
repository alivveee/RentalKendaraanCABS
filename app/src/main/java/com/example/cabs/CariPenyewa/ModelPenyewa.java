package com.example.cabs.CariPenyewa;

public class ModelPenyewa {

    private String nama,no_hp,alamat,tanggal,nama_kendaraan,uri,total;


    private String key;




//    String nama_kendaraan, String harga

    public ModelPenyewa(String nama, String no_hp,String alamat, String nama_kendaraan,String tanggal,String total,String uri) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.no_hp = no_hp;
        this.alamat = alamat;
        this.nama_kendaraan = nama_kendaraan;
        this.uri = uri;
        this.total = total;

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

//    public String getHarga() {
//        return harga;
//    }
//
//
//    public String getImage() {
//        return image;
//    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setNama_kendaraan(String nama_kendaraan) {
        this.nama_kendaraan = nama_kendaraan;
    }

//    public void setHarga(String harga) {
//        this.harga = harga;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }


    public String getUri() {
        return uri;
    }

    public String getTotal() {
        return total;
    }

    public String getKey() {
        return key;
    }
}

