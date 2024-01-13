package com.example.projeto_dam

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: dadosViewModel
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private var dados: List<DadosAuth> = ArrayList()
    private var isAuth: Boolean = false


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userNameText = findViewById<EditText>(R.id.Username)
        val passwordText = findViewById<EditText>(R.id.Password)
        val imm = getSystemService<InputMethodManager>()
        viewModel = ViewModelProvider(this).get<dadosViewModel>()
        /*val call = RetrofitInitializer().dadosResposta()!!.list()


               CoroutineScope(Dispatchers.Main).launch{

                  val response: ApiResponse = call.await()


                   withContext(Dispatchers.IO) {


                dados = response.folha2

            }

        }*/

        userNameText.requestFocus()
        imm?.showSoftInput(userNameText, InputMethodManager.SHOW_IMPLICIT)
        if (!userNameText.hasFocus()) {
            passwordText.requestFocus()
        }

        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager2)

        myViewPagerAdapter = MyViewPagerAdapter(this)
        viewPager2.adapter = myViewPagerAdapter

        tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        tabLayout.visibility = View.INVISIBLE
        viewPager2.visibility = View.INVISIBLE

        passwordText.setOnEditorActionListener { v , actionId , ev ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                imm?.hideSoftInputFromWindow(passwordText.windowToken, 0)
                /*for (i in dados.indices) {
                    Log.d(userNameText.text.toString(), "")
                    Log.d(passwordText.text.toString(), "")
                    if (userNameText.text.toString() == dados[i].Username &&
                        passwordText.text.toString() == dados[i].Password
                    ) {*/
                        viewModel.user = userNameText.text.toString()
                        isAuth = true
                    /*}
                }*/
                if (isAuth) {
                    userNameText.visibility = View.INVISIBLE
                    passwordText.visibility = View.INVISIBLE
                    passwordText.clearFocus()
                    tabLayout.visibility = View.VISIBLE
                    viewPager2.visibility = View.VISIBLE
                    Toast.makeText(
                        this@MainActivity,
                        "Acesso disponibilizado",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnEditorActionListener true
                } else {
                    Toast.makeText(this@MainActivity, "Acesso Negado", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Erro de input" ,Toast.LENGTH_SHORT).show()
            }
                return@setOnEditorActionListener false
        }
    }
}