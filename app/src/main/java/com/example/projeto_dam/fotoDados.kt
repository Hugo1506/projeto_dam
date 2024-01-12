package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class fotoDados(
    @SerializedName("fotob64")
    val fotob64: String,
    @SerializedName("user")
    val User: String,
    @SerializedName("id")
    val id: Int
)
