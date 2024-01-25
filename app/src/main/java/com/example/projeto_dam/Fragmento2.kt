package com.example.projeto_dam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fotoEnviar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragmento2.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragmento2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewModel: dadosViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(dadosViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragmento2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ler()
    }

    //volta a ler as imagens exitentes para garantir que as imagens criadas depois do inicio da app são mostradass
    override fun onResume() {
        super.onResume()
        ler()
    }

    override fun onPause() {
        super.onPause()
    }




    private fun ler() {

        val bitmapList: ArrayList<Bitmap?> = ArrayList()
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        for (i in 0 until getNumFotos()) {
            bitmapList.add(getImage(i))
        }
        val linearLayout: LinearLayout = requireView().findViewById(R.id.linear)
        //apaga todas as views anteriores
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
                linearLayout.addView(currentRow,layoutParams)

            }

            if (bitmap != null) {
                val imageView = ImageView(requireContext())
                imageView.setImageBitmap(bitmap)

                // tamanho da imagem e margens
                var imageSize = 220
                var layoutParams = LinearLayout.LayoutParams(imageSize, imageSize + 45 )
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
                    layoutParams = LinearLayout.LayoutParams(imageSize, imageSize )


                    // Cria um EditText
                    val editText = EditText(requireContext())
                    editText.imeOptions = EditorInfo.IME_ACTION_DONE
                    editText.isFocusable = true
                    editText.hint = "Digite seu texto aqui..."
                    editText.isSingleLine = true
                    editText.requestFocus()


                    // Cria um botão "Publicar"
                    val publicarButton = Button(requireContext())
                    publicarButton.setText(getString(R.string.publicar_bt))
                    editText.setOnEditorActionListener { v, action, ev ->
                        if (action == EditorInfo.IME_ACTION_DONE) {
                            imm?.hideSoftInputFromWindow(editText.windowToken, 0)
                            editText.clearFocus()
                            // Lógica para publicar
                            val textoDigitado = editText.text.toString()
                            val byteOPS = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOPS)
                            val bytes: ByteArray = byteOPS.toByteArray()
                            val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
                            val fotoDadoAEnviar = fotoDados(encoded, textoDigitado, viewModel.user)
                            val fotoDadoWrapper = fotoEnviar.fotoDadosWrapper(fotoDadoAEnviar)
                            val send = RetrofitInitializer().dadosFoto()

                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    send.publicar(fotoDadoWrapper).await()

                                    withContext(Dispatchers.IO) {
                                        Log.d("Publish", "Texto publicado: $textoDigitado")

                                    }
                                } catch (e: Exception) {
                                    Log.e("Publish", "Erro ao publicar: ${e.message}", e)

                                }
                            }
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.container, Fragmento2())
                            transaction.addToBackStack(null)
                            transaction.commit()
                            return@setOnEditorActionListener true
                        }
                        return@setOnEditorActionListener false
                    }
                    publicarButton.setOnClickListener {
                        // Lógica para publicar
                        val textoDigitado = editText.text.toString()
                        val byteOPS = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOPS)
                        val bytes: ByteArray = byteOPS.toByteArray()
                        val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
                        val fotoDadoAEnviar = fotoDados(encoded, textoDigitado, viewModel.user)
                        val fotoDadoWrapper = fotoEnviar.fotoDadosWrapper(fotoDadoAEnviar)
                        val send = RetrofitInitializer().dadosFoto()

                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                send.publicar(fotoDadoWrapper).await()

                                viewModel.fotoNova = true
                                withContext(Dispatchers.IO) {
                                    Log.d("Publish", "Texto publicado: $textoDigitado")

                                }
                            } catch (e: Exception) {
                                Log.e("Publish", "Erro ao publicar: ${e.message}", e)

                            }
                        }
                        imm?.hideSoftInputFromWindow(editText.windowToken, 0)
                        editText.clearFocus()
                        ler()
                    }




                    // Cria um botão "Voltar"
                    val voltarButton = Button(requireContext())
                    voltarButton.setText(getString(R.string.voltar_bt))
                    voltarButton.setOnClickListener {
                        imm?.hideSoftInputFromWindow(editText.windowToken, 0)
                        editText.clearFocus()
                        ler()
                    }

                    // Adiciona as views ao linearLayout
                    linearLayout.addView(clickedImageView, layoutParams)
                    linearLayout.addView(editText)
                    linearLayout.addView(publicarButton)
                    linearLayout.addView(voltarButton)
                }
            }
        }

        // Center the last row
        linearLayout.gravity = Gravity.CENTER
    }


    private fun getImage(i:Int): Bitmap? {
        val directory: File = requireContext().filesDir
        val file = File(directory, i.toString() + ".bit")

        try {
            val fi = FileInputStream(file)
            // transforma o png num bitmap
            val bitmap: Bitmap = BitmapFactory.decodeStream(fi)
            fi.close()
            // retorna a imagem do ficheiro
            return bitmap
        } catch (e: FileNotFoundException) {
            // não existe nenhum ficheiro com o nome dado
            Log.e("Publish", "Ficheiro não encontrado: ${e.message}", e)
        } catch (e: IOException) {
            // problemas a ler o ficheiro
            Log.e("Publish", "Erro ao ler o ficheiro: ${e.message}", e)
        }

        // se houver algum error devolve null
        return null
    }

    //devolve o numero de fotos
    private fun getNumFotos() : Int {
        val directory: File = requireContext().filesDir
        val f = File(directory, "nomes.txt")

            val currentNumber: Int = try {
                f.readText(Charsets.UTF_8).toInt()
            } catch (e: Exception) {
                return 0
            }
            return currentNumber

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragmento2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragmento2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}