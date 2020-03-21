package com.example.simplenews.network

import com.example.simplenews.network.news.NewsContainer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nytimes.com/svc/search/v2/"
private const val APP_ID = "vzsMQlHlaK60ZY96HyoIIaohXjmXbPKa"

interface ApiService {
    @GET("articlesearch.json")
    suspend fun getNews(@Query("q") keyword: String?,
                @Query("page") page: Int?): NewsContainer
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    // create interceptor to add api-key for every request
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder()
            .addQueryParameter("api-key", APP_ID)
            .addQueryParameter("sort", "newest")
            .addQueryParameter("fq", "source:(\"The New York Times\")")
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }

    private val apiClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    private val retrofit = Retrofit.Builder().client(apiClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(ApiService::class.java)
}