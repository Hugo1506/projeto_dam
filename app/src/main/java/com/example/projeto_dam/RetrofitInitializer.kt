package com.example.projeto_dam

import com.google.gson.GsonBuilder
import fotoEnviar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private var okHTTP = OkHttpClient.Builder().build()
    private var gson = GsonBuilder().setLenient().create()
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.sheety.co/0d32875c1e57a634cfb04534f8a4a041/vinx/")
        .client(okHTTP)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun dadosResposta(): Auth? = retrofit.create(Auth::class.java)
    fun dadosFoto(): fotoEnviar = retrofit.create(fotoEnviar::class.java)
    fun dadosFotosResposta(): fotosResposta? = retrofit.create(fotosResposta::class.java)
}