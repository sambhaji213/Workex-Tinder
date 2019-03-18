package com.sk.workextinder.network;

import com.sk.workextinder.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("helloWorld")
    Call<List<User>> getUserList();
}