package com.example.projeto_dam

data class DadosAuth(
    val username: String,
    val password: String,
    val id: Int
) {
    override fun toString(): String {
        return "DadosAuth(username='$username', password='$password', id=$id)"
    }
}