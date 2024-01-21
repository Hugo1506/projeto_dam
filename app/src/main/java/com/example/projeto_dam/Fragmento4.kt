package com.example.projeto_dam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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

    private lateinit var dados: List<fotoDados>
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //volta a ler as imagens exitentes para garantir que as imagens criadas depois do inicio da app são mostradass
    override fun onResume() {
        super.onResume()
        if(viewModel.fotoNova){
            lerFotos()
            viewModel.fotoNova = false
        } else {
            val linearLayout: LinearLayout = requireView().findViewById(R.id.verfotosLinear)
            linearLayout.removeAllViews()
            // Define que apenas podem haver 2 imagens por linha
            val imagesPerRow = 2
            var currentRow: LinearLayout? = null

            for ((index, bitmap) in viewModel.fotosF4.withIndex()) {
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
                        imageSize = 400
                        layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)

                        val voltarButton = Button(requireContext())
                        voltarButton.setText(getString(R.string.voltar_bt))
                        voltarButton.setOnClickListener {
                            linearLayout.removeAllViews()
                            lerFotos()
                        }

                        // Adiciona as views ao linearLayout
                        linearLayout.addView(voltarButton)
                        linearLayout.addView(clickedImageView, layoutParams)

                    }



            }
            // Center the last row
            linearLayout.gravity = Gravity.CENTER
        }
    }

    override fun onPause() {
        super.onPause()
        val linearLayout: LinearLayout = requireView().findViewById(R.id.verfotosLinear)
        linearLayout.removeAllViews()
    }

    private fun lerFotos() {
        val bitmapList: ArrayList<Bitmap?> = ArrayList()
        val call = RetrofitInitializer().dadosFotosResposta()!!.listFotos()


        CoroutineScope(Dispatchers.IO).launch {

            val response: ApiResponseFotos = call.await()


            withContext(Dispatchers.Main) {


                dados = response.folha1
                for (i in dados.indices) {
                    if (dados[i].User == viewModel.user){
                        val cleanImage: String = dados[i].fotob64.replace("data:image/png;base64,", "")
                            .replace("data:image/jpeg;base64,", "")
                        val bytes: ByteArray = Base64.decode(cleanImage, Base64.DEFAULT)
                        bitmapList.add(BitmapFactory.decodeByteArray(bytes , 0, bytes.size ))
                        if(!viewModel.fotosF4.contains(BitmapFactory.decodeByteArray(bytes , 0, bytes.size ))) {
                            viewModel.fotosF4.add(
                                BitmapFactory.decodeByteArray(
                                    bytes,
                                    0,
                                    bytes.size
                                )
                            )
                        }
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
                            imageSize = 400
                            layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)

                            val voltarButton = Button(requireContext())
                            voltarButton.setText(getString(R.string.voltar_bt))
                            voltarButton.setOnClickListener {
                                linearLayout.removeAllViews()
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.container, Fragmento4())
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


    }

}