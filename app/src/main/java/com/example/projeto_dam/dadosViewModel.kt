package com.example.projeto_dam

import androidx.lifecycle.ViewModel

class dadosViewModel(
) : ViewModel() {
    var user: String = ""
    var fotoNova: Boolean = false
    var dados: ArrayList<fotoDados> = ArrayList()
    var descrF4: ArrayList<String> = ArrayList()
    var userId: Int = 0
}