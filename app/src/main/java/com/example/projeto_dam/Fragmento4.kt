package com.example.projeto_dam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class Fragmento4 : Fragment() {

    private var dados: List<fotoDados> = ArrayList()
    lateinit var viewModel: dadosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(dadosViewModel::class.java)
        lerFotos()
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragmento4, container, false)
    }

    private fun lerFotos() {
        val bitmapList: ArrayList<Bitmap?> = ArrayList()
        val call = RetrofitInitializer().dadosFotosResposta()!!.listFotos()


        CoroutineScope(Dispatchers.Main).launch {

            val response: ApiResponseFotos = call.await()


            withContext(Dispatchers.IO) {


                dados = response.folha1

            }
        }

        for (i in dados.indices) {
            if (dados[i].User == viewModel.user){
           bitmapList.add(BitmapFactory.decodeByteArray(dados[i].fotob64.toByteArray(), 0, 100 ))
                }
        }
        val linearLayout: LinearLayout = requireView().findViewById(R.id.verfotosLinear)
        linearLayout.removeAllViews()
        // Define que apenas podem haver 2 imagens por linha
        val imagesPerRow = 2
        var currentRow: LinearLayout? = null

        for ((index, bitmap) in bitmapList.withIndex()) {
            // quando o index é par ou seja de duas em duas imagens é criado uma nova linha que vai armazenar as imagens
            if (index % imagesPerRow == 0) {
                // cria uma nova linha para armazenar as imagens
                currentRow = LinearLayout(requireContext())
                currentRow.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.addView(currentRow, layoutParams)

            }
            if (bitmap != null) {
                val imageView = ImageView(requireContext())
                imageView.setImageBitmap(bitmap)

                // tamanho da imagem e margens
                var imageSize = 220
                var layoutParams = LinearLayout.LayoutParams(imageSize, imageSize + 45)
                layoutParams.setMargins(10, 0, 0, 50)

                currentRow?.addView(imageView, layoutParams)

                // quando uma imagem é clicada
                imageView.setOnClickListener {
                    // Remove todas as views existentes do linearLayout
                    linearLayout.removeAllViews()
                    val clickedImageView = ImageView(requireContext())
                    clickedImageView.setImageBitmap(bitmap)

                    // tamanho da imagem
                    imageSize = 500
                    layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)

                    val voltarButton = Button(requireContext())
                    voltarButton.setText(getString(R.string.voltar_bt))
                    voltarButton.setOnClickListener {
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        linearLayout.removeAllViews()
                        transaction.replace(R.id.container, Fragmento2())
                        transaction.addToBackStack(null)
                        transaction.commit()                    }

                    // Adiciona as views ao linearLayout
                    linearLayout.addView(clickedImageView, layoutParams)
                    linearLayout.addView(voltarButton)

                }

            }

        }
        // Center the last row
        linearLayout.gravity = Gravity.CENTER
    }

}