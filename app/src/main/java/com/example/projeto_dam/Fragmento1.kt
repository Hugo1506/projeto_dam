package com.example.projeto_dam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.util.Scanner


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragmento1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragmento1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var button: Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fragmento1, container, false)
        button = view.findViewById(R.id.butFrag1)
        imageView = view.findViewById(R.id.imagem)

        button.setOnClickListener {
            tirarFoto()
        }

        return view
    }

    private fun tirarFoto() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)
    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            escrever(data?.extras?.get("data") as Bitmap,currentNameFile() )
            imageView.setImageBitmap(ler("imagem.bit"))
        }
    }

    /**
    * guarda a imagem no armazenamento interno
    * */
    fun escrever(imagem : Bitmap, nome: String) {
        // escrita no internal Storage
        val directory: File = requireContext().filesDir
        val file: File  = File(directory, "$nome.bit")
        val fo: FileOutputStream = FileOutputStream(file)
        // comprime a imagem em bitmap para um png
        imagem.compress(Bitmap.CompressFormat.PNG, 100, fo)
        fo.close()

        // aumenta o index que dará o nome do próximo ficheiro
        val dir: File = requireContext().filesDir
        val f: File  = File(directory, "nomes.txt")
        val foNomes: FileOutputStream = FileOutputStream(f)
        val currentNumber: Int = try {
            FileInputStream(f).bufferedReader().use { it.readText().toInt() }
        } catch (e: FileNotFoundException) {
            0 // se o ficheiro não exitir, ele é criado com "0"
        } catch (e: NumberFormatException) {
            0 // outros problemas metem o numero a 0
        }
        // aumenta o valor
        val newNumber = currentNumber + 1

        // escreve o novo valor
        f.writeText(newNumber.toString())
        Toast.makeText(requireContext(), FileInputStream(f).bufferedReader().use { it.readText() }, Toast.LENGTH_LONG).show()
        foNomes.close()
    }


    /**
     * leitura do armazenamento interno
     */
    fun ler(nome: String): Bitmap? {
        val directory: File = requireContext().filesDir
        val file: File = File(directory, nome)

        try {
            val fi: FileInputStream = FileInputStream(file)
            // transforma o png num bitmap
            val bitmap: Bitmap = BitmapFactory.decodeStream(fi)
            fi.close()
            // retorna a imagem do ficheiro
            return bitmap
        } catch (e: FileNotFoundException) {
            // não existe nenhum ficheiro com o nome dado
            Toast.makeText(requireContext(), "ficheiro não encontrado", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            // preblemas a ler o ficheiro
            Toast.makeText(requireContext(), "Erro a ler o ficheiro", Toast.LENGTH_LONG).show()
        }

        // se houver algum error devolve null
        return null
    }

    fun currentNameFile(): String {
        val dir: File = requireContext().filesDir
        val file: File = File(dir, "nomes.txt")

        val currentNumber: Int = try {
            FileInputStream(file).bufferedReader().use { it.readText().toInt() }
        } catch (e: FileNotFoundException) {
            return "0"
        } catch (e: NumberFormatException) {
            return "0"
        }
        return currentNumber.toString()
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragmento1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragmento1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}