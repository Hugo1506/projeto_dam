package com.example.projeto_dam

import com.google.gson.annotations.SerializedName

data class DadosAuth(
    @SerializedName("username")
    val Username: String,
    @SerializedName("password")
    val Password: String,
    @SerializedName("id")
    val id: Int
) {
    override fun toString(): String {
        return "DadosAuth(username='$Username', password='$Password', id=$id)"
    }
}