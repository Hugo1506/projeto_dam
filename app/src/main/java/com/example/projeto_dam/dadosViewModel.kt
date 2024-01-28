package com.example.projeto_dam

import androidx.lifecycle.ViewModel

class dadosViewModel(
) : ViewModel() {
    var user: String = ""
    var fotoNova: Boolean = false
    var dados: ArrayList<fotoDados> = ArrayList()
    var Id: ArrayList<Int> = ArrayList()
}