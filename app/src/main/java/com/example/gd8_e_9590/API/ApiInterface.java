package com.example.gd8_e_9590.API;

import com.example.gd8_e_9590.MODEL.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user")
    Call<UserResponse> getAllUser(@Query("data") String data);

    @GET("user/{id}")
    Call<UserResponse> getUserById(@Path("id") String id,
                                   @Query("data") String data);

    @POST("user")
    @FormUrlEncoded
    Call<UserResponse> createUser(@Field("nama") String nama,
                                  @Field("nim") String nim,
                                  @Field("prodi") String prodi,
                                  @Field("fakultas") String fakultas,
                                  @Field("jenis_kelamin") String jenis_kelamin,
                                  @Field("password") String password);

    @POST("user/delete/{id}")
    @FormUrlEncoded
    Call<UserResponse> deleteUser(@Path("id") String id);
}
