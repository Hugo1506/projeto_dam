package com.example.projeto_dam

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

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

        val registarButtn = Button(this)
        registarButtn.text = "Registar"
        registarButtn.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        registarButtn.setOnClickListener {

        }
        //mete o button no layout
        val container = findViewById<RelativeLayout>(R.id.container)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.BELOW, R.id.Password)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        registarButtn.layoutParams = params
        container.addView(registarButtn)


        registarButtn.setOnClickListener(){
            userNameText.visibility = View.GONE
            passwordText.visibility = View.GONE
            registarButtn.visibility = View.GONE

            //cria o editText do username
            val usernameRegist = EditText(this)
            usernameRegist.id = View.generateViewId()
            usernameRegist.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            usernameRegist.hint = "username a registar"
            usernameRegist.inputType = InputType.TYPE_CLASS_TEXT
            container.addView(usernameRegist)

            // cria o editText da password
            val passwordRegist = EditText(this)
            passwordRegist.id = View.generateViewId()
            passwordRegist.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            passwordRegist.hint = "password"
            passwordRegist.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            //mete os novos editText no layout
            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.BELOW, usernameRegist.id)
            passwordRegist.layoutParams = params
            container.addView(passwordRegist)

            // criar o botão Login
            val loginButton = Button(this)
            loginButton.text = "Login"
            loginButton.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            loginButton.setOnClickListener {
                //mostra a página de login
                userNameText.visibility = View.VISIBLE
                passwordText.visibility = View.VISIBLE
                registarButtn.visibility = View.VISIBLE

                //remove a parte referente ao registo
                container.removeView(usernameRegist)
                container.removeView(passwordRegist)
                container.removeView(loginButton)

            }
            // adiciona o botão debaixo das editText
            val paramsLoginButton = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            paramsLoginButton.addRule(RelativeLayout.BELOW, passwordRegist.id)
            loginButton.layoutParams = paramsLoginButton
            container.addView(loginButton)

        }
        val call = RetrofitInitializer().dadosResposta()!!.list()


               CoroutineScope(Dispatchers.Main).launch{

                  val response: ApiResponse = call.await()


                   withContext(Dispatchers.IO) {


                dados = response.folha2

            }

        }

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
        val registaButton = Button(this)
        val loginButton = Button(this)
        registaButton.text = "Registar"
        loginButton.text = "Login"
        registaButton.layoutParams = ViewGroup.LayoutParams(15, 40)
        loginButton.layoutParams = ViewGroup.LayoutParams(15, 40)
        registaButton

        passwordText.setOnEditorActionListener { v , actionId , ev ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                imm?.hideSoftInputFromWindow(passwordText.windowToken, 0)
                for (i in dados.indices) {
                    Log.d(userNameText.text.toString(), "")
                    Log.d(passwordText.text.toString(), "")
                    if (userNameText.text.toString() == dados[i].Username &&
                        passwordText.text.toString() == dados[i].Password
                    ) {
                        viewModel.user = userNameText.text.toString()
                        isAuth = true
                    }
                }
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