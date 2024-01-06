package com.example.projeto_dam

data class DadosAuth(
    val Username: String,
    val Password: String,
    val id: Int
) {
    override fun toString(): String {
        return "DadosAuth(username='$Username', password='$Password', id=$id)"
    }
}