package com.festum.nearbybusiness.owner.ui.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    val BASE_URL = "http://192.168.1.70:7600/api/v1/"


    var retrofitService: RetrofitClient? = null

/*
    fun getInstance() : RetrofitClient {
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitService = retrofit.create(RetrofitClient::class.java)
        }
        return retrofitService!!
    }
*/


     fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIInterface = getRetrofit().create(APIInterface::class.java)
}