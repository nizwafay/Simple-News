package com.example.simplenews.network

import com.example.simplenews.network.news.NewsContainer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
private const val APP_ID = "2ff90da4-77ae-4726-8230-868531f98733"

interface ApiService {
    @GET("articlesearch.json")
    fun getNews(@Query("q") keyword: String,
                @Query("page") page: Int = 0): Call<NewsContainer>
}

object Network {
    // create interceptor to add api-key for every request
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder().addQueryParameter("api-key", APP_ID).build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }

    private val apiClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder().client(apiClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val service = retrofit.create(ApiService::class.java)
}