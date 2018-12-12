package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class NewMember {
    @SerializedName("id_member")
    private String id_member;
    @SerializedName("nama")
    private String nama;
    @SerializedName("pekerjaan")
    private String pekerjaan;
    @SerializedName("tempat_kerja")
    private String tempat_kerja;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getTempat_kerja() {
        return tempat_kerja;
    }

    public void setTempat_kerja(String tempat_kerja) {
        this.tempat_kerja = tempat_kerja;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
