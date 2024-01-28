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
        .baseUrl("https://api.sheety.co/bfe225a3b1923b39d4fc081774e18524/vinx/")
        .client(okHTTP)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun dadosResposta(): Auth? = retrofit.create(Auth::class.java)
    fun dadosFoto(): fotoEnviar = retrofit.create(fotoEnviar::class.java)
    fun dadosFotosResposta(): fotosResposta? = retrofit.create(fotosResposta::class.java)
    fun editarDescricao(): editarDescr? = retrofit.create(editarDescr::class.java)
    fun deleteService(): DeleteService? = retrofit.create(DeleteService::class.java)
    fun registarUser(): RegistarAuth = retrofit.create(RegistarAuth::class.java)
}