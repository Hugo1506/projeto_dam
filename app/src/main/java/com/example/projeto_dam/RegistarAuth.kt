package com.example.projeto_dam

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistarAuth {
    @POST("folha2/")
    fun registar(@Body dados: registaWrapper): Call<registaWrapper>


    data class registaWrapper(
        @SerializedName("folha2")
        val folha2: DadosRegistar
    )
}