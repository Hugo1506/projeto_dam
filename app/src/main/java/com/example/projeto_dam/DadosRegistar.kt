package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class DadosRegistar(
    @SerializedName("username")
    val Username: String,
    @SerializedName("password")
    val Password: String
) {
    override fun toString(): String {
        return "DadosAuth(username='$Username', password='$Password')"
    }
}