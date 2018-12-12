package com.example.jamesalexander.myapplication.Rest;

import com.example.jamesalexander.myapplication.Model.GetEvent;
import com.example.jamesalexander.myapplication.Model.GetEventMember;
import com.example.jamesalexander.myapplication.Model.GetNewMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelEventMember;
import com.example.jamesalexander.myapplication.Model.PostPutDelNewMember;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("newmember")
    Call<GetNewMember> getNewMember(@Query("id_member") String id_member);
    @FormUrlEncoded
    @POST("newmember")
    Call<PostPutDelNewMember> postNewMember(@Field("id_member") String id_member,
                                            @Field("nama") String nama,
                                            @Field("pekerjaan") String pekerjaan,
                                            @Field("tempat_kerja") String tempat_kerja,
                                            @Field("email") String email,
                                            @Field("phone") String phone);
    @GET("eventmember")
    Call<GetEventMember> getEventMember(@Query("id_member") String id_member, @Query("id_event") String id_event);
    @FormUrlEncoded
    @POST("eventmember")
    Call<PostPutDelEventMember> postEventMember(@Field("id_member") String id_member,
                                              @Field("id_event") String id_event,
                                              @Field("tgl_daftar") String tgl_daftar,
                                                @Field("alamat") String alamat,
                                                @Field("status_hadir") String status_hadir);
    @FormUrlEncoded
    @PUT("eventmember")
    Call<PostPutDelEventMember> putEventMember(@Field("id_member") String id_member,
                                        @Field("id_event") String id_event,
                                        @Field("status_hadir") String status_hadir);
    @GET("event")
    Call<GetEvent> getEvent(@Query("id_event") String id_event);
    /*@FormUrlEncoded
    @HTTP(method = "DELETE", path = "kontak", hasBody = true)
    Call<PostPutDelNewMember> deleteKontak(@Field("id") String id);*/
}
