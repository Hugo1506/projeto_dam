package com.example.projeto_dam

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
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

    private lateinit var dados: List<fotoDadosParaEdit>
    lateinit var viewModel: dadosViewModel
    private var currentRow: LinearLayout? = null
    private lateinit var linearLayout: LinearLayout
    var editedText: String = ""

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
                var cont = 0
                for (i in dados.indices) {
                    if (dados[i].User == viewModel.user){

                        val cleanImage: String = dados[i].fotob64.replace("data:image/png;base64,", "")
                            .replace("data:image/jpeg;base64,", "")
                        val bytes: ByteArray = Base64.decode(cleanImage, Base64.DEFAULT)
                        bitmapList.add(BitmapFactory.decodeByteArray(bytes , 0, bytes.size ))
                        viewModel.dados.add(fotoDados(dados[i].fotob64, dados[i].Descricao, viewModel.user ))
                        if(!viewModel.dados[cont].fotob64.equals(BitmapFactory.decodeByteArray(bytes , 0, bytes.size ))) {
                            cont = cont.inc()
                            viewModel.dados.add(fotoDados(dados[i].fotob64,dados[i].Descricao, viewModel.user ))
                            viewModel.Id.add(dados[i].Id)
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
                            desc.text = viewModel.dados[index].Descricao

                            val voltarButton = Button(requireContext())
                            voltarButton.text = getString(R.string.voltar_bt)
                            voltarButton.setOnClickListener {
                                lerFotos()
                            }

                            val editarButton = Button(requireContext())
                            editarButton.text = "editar"

                            val apagarButton = Button(requireContext())
                            apagarButton.text = "apagar"



                            // Adiciona as views ao linearLayout
                            linearLayout.addView(clickedImageView, layoutParams)
                            linearLayout.addView(voltarButton)
                            linearLayout.addView(editarButton)
                            linearLayout.addView(apagarButton)
                            linearLayout.addView(desc)


                            //listener do editar
                            editarButton.setOnClickListener {
                                val imm = requireContext().getSystemService<InputMethodManager>(InputMethodManager::class.java)
                                //criar o editText
                                val edit = EditText(requireContext())
                                edit.inputType = InputType.TYPE_CLASS_TEXT
                                edit.imeOptions = EditorInfo.IME_ACTION_DONE
                                edit.hint = "Texto Editado"
                                imm.showSoftInput( edit, InputMethodManager.SHOW_IMPLICIT)

                                //mete o foco no editText
                                edit.requestFocus()

                                edit.setOnEditorActionListener { _, actionId, _ ->
                                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                                        editedText = edit.text.toString()

                                        imm.hideSoftInputFromWindow(edit.windowToken, 0)

                                        val dadosEdit = fotoDadosEditar(editedText)
                                        val send = RetrofitInitializer().editarDescricao()
                                        CoroutineScope(Dispatchers.Main).launch {
                                            try {
                                                send?.editarDesc(viewModel.Id[index], editarDescr.descEditWrapper(dadosEdit))
                                                    ?.await()

                                                withContext(Dispatchers.IO) {
                                                    Log.d("Edit", "Texto editado: $editedText")

                                                }
                                            } catch (e: Exception) {
                                                Log.e("Edit", "Erro ao editar: ${e.message}", e)

                                            }
                                        }
                                        return@setOnEditorActionListener true
                                    } else {
                                        return@setOnEditorActionListener false
                                    }
                                }



                                linearLayout.removeView(desc)
                                linearLayout.addView(edit)
                            }


                            apagarButton.setOnClickListener(){
                                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                                alertDialogBuilder.setTitle("Confirmação")
                                alertDialogBuilder.setMessage("Tem certeza que deseja apagar esta imagem?")

                                alertDialogBuilder.setPositiveButton("Sim") { dialog, which ->
                                    val deleteService = RetrofitInitializer().deleteService()

                                    CoroutineScope(Dispatchers.Main).launch {
                                        try {
                                            deleteService?.deleteRow(viewModel.Id[index])

                                            withContext(Dispatchers.IO) {
                                            }
                                        } catch (e: Exception) {
                                            Log.e("Delete", "Error deleting row: ${e.message}", e)
                                        }

                                        lerFotos()
                                    }
                                }

                                alertDialogBuilder.setNegativeButton("Não") { dialog, which ->
                                    dialog.dismiss()
                                }

                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }
                        }

                    }

                }
                linearLayout.gravity = Gravity.CENTER

            }

        }


    }

}