package com.example.projeto_dam

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
interface Auth {
    @SerializedName("folha2")
    @GET("folha2")
    suspend fun list(): Call<List<DadosAuth>>
}