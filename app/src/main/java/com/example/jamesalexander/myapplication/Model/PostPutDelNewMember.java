package com.example.jamesalexander.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class PostPutDelNewMember {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    NewMember mMember;
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
    public NewMember getmMember() {
        return mMember;
    }

    public void setmMember(NewMember mMember) {
        this.mMember = mMember;
    }
}
