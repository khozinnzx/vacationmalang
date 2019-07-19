package com.example.vacationmalangver1;

import com.example.vacationmalangver1.Model.ResponsModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiMysql {
    @GET("view_data.php")
    Call<ResponsModel>getData();
}
