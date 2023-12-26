package com.example.projeto_dam

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
        return inflater.inflate(R.layout.fragment_fragmento2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ler()
    }


    fun ler (){
        val bitmapList: ArrayList<Bitmap?> = ArrayList()
        for (i in 0..<getNumFotos()){
            bitmapList.add(getImage(i))
        }
        val linearLayout: LinearLayout = requireView().findViewById(R.id.linear)

        for (bitmap in bitmapList) {
            if (bitmap != null) {
                val imageView = ImageView(requireContext())
                imageView.setImageBitmap(bitmap)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                linearLayout.addView(imageView, layoutParams)
            }
        }
    }
    private fun getImage(i:Int): Bitmap? {
        val directory: File = requireContext().filesDir
        val file: File = File(directory, i.toString() + ".bit")

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
            // problemas a ler o ficheiro
            Toast.makeText(requireContext(), "Erro a ler o ficheiro", Toast.LENGTH_LONG).show()
        }

        // se houver algum error devolve null
        return null
    }

    fun getNumFotos() : Int {
        val directory: File = requireContext().filesDir
        val f: File = File(directory, "nomes.txt")

            val fi: FileInputStream = FileInputStream(f)
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