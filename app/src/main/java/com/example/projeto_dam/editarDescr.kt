package com.example.projeto_dam

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface editarDescr {
    @PUT("folha1/")
    fun editarDesc(@Body dadosEdit: descEditWrapper): Call<fotoDadosEditar>

    data class descEditWrapper(
        @SerializedName("folha1")
        val folha1: fotoDadosEditar
    )

}