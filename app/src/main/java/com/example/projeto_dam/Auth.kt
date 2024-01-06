package com.example.projeto_dam

import retrofit2.Call
import retrofit2.http.GET
interface Auth {
    @GET("folha2/")
    suspend fun list(): Call<List<DadosAuth>>
}