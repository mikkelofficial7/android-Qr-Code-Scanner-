package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class EventMember {
    @SerializedName("id_member")
    private String id_member;
    @SerializedName("id_event")
    private String id_event;
    @SerializedName("tgl_daftar")
    private String tgl_daftar;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("status_hadir")
    private String status_hadir;

    public String getId_member() {
        return id_member;
    }

    public void setId_member(String id_member) {
        this.id_member = id_member;
    }

    public String getId_event() {
        return id_event;
    }

    public void setId_event(String id_event) {
        this.id_event = id_event;
    }

    public String getTgl_daftar() {
        return tgl_daftar;
    }

    public void setTgl_daftar(String tgl_daftar) {
        this.tgl_daftar = tgl_daftar;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public String getStatus_hadir() {
        return status_hadir;
    }

    public void setStatus_hadir(String status_hadir) {
        this.status_hadir = status_hadir;
    }
}
