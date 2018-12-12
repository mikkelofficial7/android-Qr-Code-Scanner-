package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("id_event")
    private String id_event;
    @SerializedName("alamat_event")
    private String alamat_event;
    @SerializedName("start_registration")
    private String start_registration;
    @SerializedName("end_registration")
    private String end_registration;
    public String getId_event() {
        return id_event;
    }

    public void setId_event(String id_event) {
        this.id_event = id_event;
    }

    public String getAlamat_event() {
        return alamat_event;
    }

    public void setAlamat_event(String alamat_event) {
        this.alamat_event = alamat_event;
    }
    public String getStart_registration() {
        return start_registration;
    }

    public void setStart_registration(String start_registration) {
        this.start_registration = start_registration;
    }

    public String getEnd_registration() {
        return end_registration;
    }

    public void setEnd_registration(String end_registration) {
        this.end_registration = end_registration;
    }
}
