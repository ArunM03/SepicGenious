package com.sepicgenious.apis;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApis {

    @Headers("Content-Type: application/json")
    @POST("/loginApp/checkLogin")
    Call<ResponseBody> login(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/loginApp/changePass")
    Call<ResponseBody> changePass(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/searchBook")
    Call<ResponseBody> searchBook(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/publicPrivateBooks")
    Call<ResponseBody> publicPrivateBooks(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/singleBook")
    Call<ResponseBody> singleBook(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/landing")
    Call<ResponseBody> landingpageGrade(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book")
    Call<ResponseBody> allBook(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/buyBook")
    Call<ResponseBody> buyBook(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/sync")
    Call<ResponseBody> syncData(@Body RequestBody body);

    @Headers("Content-Type: application/json")
    @POST("/book/purchasedBook")
    Call<ResponseBody> purchasedBook(@Body RequestBody body);


}
