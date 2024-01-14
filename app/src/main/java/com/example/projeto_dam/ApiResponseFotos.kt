package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class ApiResponseFotos(
    @SerializedName("folha1")
    val folha1: List<fotoDados>
)
