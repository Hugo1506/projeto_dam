package com.example.projeto_dam

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RetrofitInitializer {
    private var gson: Gson = GsonBuilder().setLenient().create()
    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .build()
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.sheety.co/f78f66f4c4513d698d0e13096825199b/vinx/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
    fun dadosService(): Auth? = retrofit.create(Auth::class.java)
}