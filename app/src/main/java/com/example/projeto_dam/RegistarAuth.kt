package com.example.projeto_dam

import retrofit2.Call
import retrofit2.http.POST

interface RegistarAuth {
    @POST("folha2/")
    fun registar(): Call<DadosRegistar>
}