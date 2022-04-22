package com.chemical_son.muslim;

import com.chemical_son.muslim.DayData.OneDay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v1/timings/now")
    public Call<OneDay> getDay(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("method") int method);
}
