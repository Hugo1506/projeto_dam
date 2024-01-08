package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("folha2")
    val folha2: List<DadosAuth>
)