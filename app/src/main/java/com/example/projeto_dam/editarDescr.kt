package com.example.projeto_dam

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface editarDescr {
    @PUT("folha1/{id}")
    fun editarDesc(@Path("id") id: Int, @Body dadosEdit: descEditWrapper): Call<fotoDadosEditar>

    data class descEditWrapper(
        @SerializedName("folha1")
        val folha1: fotoDadosEditar
    )

}