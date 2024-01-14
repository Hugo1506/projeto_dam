package com.example.projeto_dam

import retrofit2.Call
import retrofit2.http.GET

interface fotosResposta {
    @GET("folha1/")
    fun listFotos(): Call<ApiResponseFotos>
}