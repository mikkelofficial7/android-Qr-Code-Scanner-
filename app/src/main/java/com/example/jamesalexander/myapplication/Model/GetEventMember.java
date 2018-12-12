package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEventMember {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<EventMember> listNewMember;
    @SerializedName("message")
    String message;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<EventMember> getListNewMember() {
        return listNewMember;
    }

    public void setListNewMember(List<EventMember> listNewMember) {
        this.listNewMember = listNewMember;
    }
}
