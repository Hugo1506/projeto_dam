package com.example.projeto_dam

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteService {
    @DELETE("folha1/{rowId}")
    suspend fun deleteRow(@Path("rowId") rowId: Int): Response<Void>
}
