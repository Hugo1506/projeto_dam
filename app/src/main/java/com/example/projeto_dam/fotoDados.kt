package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class fotoDados(
    @SerializedName("fotos")
    val fotob64: String,
    @SerializedName("descricao")
    val Descricao: String,
    @SerializedName("user")
    val User: String
)
