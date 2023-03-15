package com.pandasdroid.rpi;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    public static String BASE_URL = "http://192.168.99.67/";
    //update_status?status=red

    @GET("update_status")
    Call<ResponseBody> UpdateStatus(@Query("status") String status, @Query("booth") String booth);
}
