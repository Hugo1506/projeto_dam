package com.example.projeto_dam

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface fotoEnviar {
    @POST("folha1/")
    fun publicar(@Body dados: fotoDados ) : Call<fotoDados>
}