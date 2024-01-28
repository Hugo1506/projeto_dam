package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class fotoDadosEditar(
    @SerializedName("descricao")
    var Descricao: String,
    @SerializedName("id")
    var Id: Int
)
