package com.example.projeto_dam

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

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

        passwordText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val call = RetrofitInitializer().dadosService()?.list()
                call?.enqueue(object : Callback<List<DadosAuth>?> {
                    override fun onResponse(
                        call: Call<List<DadosAuth>?>,
                        response: Response<List<DadosAuth>?>
                    ) {
                        response.body()?.let {
                            dados = it
                        }
                    }

                    override fun onFailure(call: Call<List<DadosAuth>?>, t: Throwable) {
                        t.message?.let {
                            Log.e("onFailure error", it)
                        }
                    }
                })

                imm?.hideSoftInputFromWindow(passwordText.windowToken, 0)

                for (i in dados.indices) {
                    Log.d(userNameText.text.toString(),"")
                    Log.d(passwordText.text.toString(),"")
                    if (userNameText.text.toString() == dados[i].Username &&
                        passwordText.text.toString() == dados[i].Password
                    ) {
                        auth = true
                        break
                    }
                }

                if (auth) {
                    userNameText.visibility = View.INVISIBLE
                    passwordText.visibility = View.INVISIBLE
                    passwordText.clearFocus()
                    tabLayout.visibility = View.VISIBLE
                    viewPager2.visibility = View.VISIBLE
                    Toast.makeText(
                        this.applicationContext,
                        "Acesso disponibilizado",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this.applicationContext, "Acesso Negado", Toast.LENGTH_LONG).show()
                }

                true
            } else {
                false
            }
        }
    }
}
