package com.example.projeto_dam

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private var dados: List<DadosAuth> = ArrayList()
    private var auth: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userNameText = findViewById<EditText>(R.id.Username)
        val passwordText = findViewById<EditText>(R.id.Password)
        val imm = getSystemService<InputMethodManager>()

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

        imm?.hideSoftInputFromWindow(passwordText.windowToken, 0)
        val call = RetrofitInitializer().dadosService()
                val response = call!!.list().execute()

                    if (response?.isSuccessful == true) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            dados = apiResponse.folha2
                            for (i in dados.indices) {
                                if (userNameText.text.toString() == dados[i].Username &&
                                    passwordText.text.toString() == dados[i].Password
                                ) {
                                    auth = true
                                }
                            }
                            if (auth) {
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
                            } else {
                                Toast.makeText(this@MainActivity, "Acesso Negado", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Empty or null response body", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Erro de Api", Toast.LENGTH_SHORT).show()
                    }
                }

        }

