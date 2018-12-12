package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEvent {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Event> listEvent;
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
    public List<Event> getListEvent() {
        return listEvent;
    }

    public void setListEvent(List<Event> listEvent) {
        this.listEvent = listEvent;
    }
}
