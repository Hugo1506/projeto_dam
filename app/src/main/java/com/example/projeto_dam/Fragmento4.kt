package com.example.projeto_dam

import android.content.res.Configuration
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
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
    private var currentRow: LinearLayout? = null
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(dadosViewModel::class.java)
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

        val isDarkMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        // muda a cor do backgroud dependento
        val scrollView: ScrollView = view.findViewById(R.id.scroll)
        if (!isDarkMode) {
            scrollView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.background_light))
        }


        linearLayout = requireView().findViewById(R.id.verfotosLinear)
        lerFotos()
    }

    //volta a ler as imagens existentes para garantir que as imagens criadas depois do inicio da app são mostradas
    override fun onResume() {
        super.onResume()
        lerFotos()
    }

    override fun onPause() {
        super.onPause()
        currentRow?.removeAllViews()
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
                        val descricao = dados[i].Descricao
                        if(!viewModel.fotosF4.contains(BitmapFactory.decodeByteArray(bytes , 0, bytes.size ))) {
                            viewModel.fotosF4.add(
                                BitmapFactory.decodeByteArray(
                                    bytes,
                                    0,
                                    bytes.size
                                )
                            )
                            viewModel.descrF4.add(descricao)
                        }
                    }
                }
                linearLayout.removeAllViews()
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.setPadding(5,5,5,5)
                // Define que apenas podem haver 2 imagens por linha
                val imagesPerRow = 2
                currentRow?.removeAllViews()

                for ((index, bitmap) in bitmapList.withIndex()) {
                    // quando o index é par ou seja de duas em duas imagens é criado uma nova linha que vai armazenar as imagens
                    if (index % imagesPerRow == 0) {
                        // cria uma nova linha para armazenar as imagens
                        currentRow = LinearLayout(requireContext())
                        currentRow!!.orientation = LinearLayout.HORIZONTAL
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
                        layoutParams.setMargins(10, 5, 0, 50)

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

                            val desc = TextView(requireContext())
                            desc.text = viewModel.descrF4[index]

                            val voltarButton = Button(requireContext())
                            voltarButton.setText(getString(R.string.voltar_bt))
                            voltarButton.setOnClickListener {
                                lerFotos()
                            }

                            // Adiciona as views ao linearLayout
                            linearLayout.addView(clickedImageView, layoutParams)
                            linearLayout.addView(voltarButton)
                            linearLayout.addView(desc)

                        }

                    }

                }
                // Center the last row
                linearLayout.gravity = Gravity.CENTER

            }
        }


    }

}