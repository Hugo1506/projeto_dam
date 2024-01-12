package com.example.projeto_dam

import retrofit2.http.POST

interface fotoEnviar {
    @POST("folha1/")
    fun publicar(dados: fotoDados)
}