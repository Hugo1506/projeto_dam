package com.example.projeto_dam

import retrofit2.Call
import retrofit2.http.GET
interface Auth {
    @GET("Folha2")
    fun list(): Call<List<DadosAuth>>
}