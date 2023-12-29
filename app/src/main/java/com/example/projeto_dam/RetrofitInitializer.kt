package com.example.projeto_dam

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.sheety.co/f78f66f4c4513d698d0e13096825199b/vinx/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    fun noteService(): Auth? = retrofit.create(Auth::class.java)
}