package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class fotoDadosParaEdit(
    @SerializedName("fotos")
    var fotob64: String,
    @SerializedName("descricao")
    var Descricao: String,
    @SerializedName("user")
    var User: String,
    @SerializedName("id")
    var Id: Int
)
