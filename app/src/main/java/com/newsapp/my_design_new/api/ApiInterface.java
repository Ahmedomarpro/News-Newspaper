package com.newsapp.my_design_new.api;

import com.newsapp.my_design_new.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("top-headlines")
    Call<News> getNews(

            @Query("country") String country ,
            @Query("apiKey") String apiKey


    );

    @GET("top-headlines")
    Call<News> getNewsbysources(

            @Query("sources") String techcrunch ,
            @Query("apiKey") String apiKey


    );

    @GET("top-headlines")
    Call<News> getNewsbySports(
            @Query("country") String eg,
            @Query("category") String sports,
            @Query("apiKey") String apiKey);

    //   @Query("sortBy") String sortBy,



    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey);





}
