package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNewMember {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<NewMember> listNewMember;
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
    public List<NewMember> getListNewMember() {
        return listNewMember;
    }

    public void setListNewMember(List<NewMember> listNewMember) {
        this.listNewMember = listNewMember;
    }
}
