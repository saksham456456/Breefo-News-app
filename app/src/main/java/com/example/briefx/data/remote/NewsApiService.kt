package com.example.briefx.data.remote

import com.example.briefx.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String? = null,
        @Query("q") query: String? = null,
        @Query("apiKey") apiKey: String = "95027c8988fb4eb19747da2a19fd867d"
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = "95027c8988fb4eb19747da2a19fd867d"
    ): Response<NewsResponse>
}