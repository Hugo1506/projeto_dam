package com.example.projeto_dam

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class dadosViewModel(
) : ViewModel() {
    var user: String = ""
    var fotoNova: Boolean = false
    var fotosF4: ArrayList<Bitmap> = ArrayList()
    var descrF4: ArrayList<String> = ArrayList()
}