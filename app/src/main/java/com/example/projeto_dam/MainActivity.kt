package  com.example.projeto_dam

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var myViewPagerAdapter: MyViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var notes: List<DadosAuth> = ArrayList()

        val userNameText = findViewById<EditText>(R.id.Username)
        val passwordText = findViewById<EditText>(R.id.Password)
        passwordText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && userNameText.text.isNotEmpty()) {
                val call = RetrofitInitializer().noteService()?.list()
                call?.enqueue(object : Callback<List<DadosAuth>?> {
                    override fun onResponse(
                        call: Call<List<DadosAuth>?>,
                        response: Response<List<DadosAuth>?>
                    ) {
                        response.body()?.let {
                            notes = it

                        }
                    }

                    override fun onFailure(call: Call<List<DadosAuth>?>, t: Throwable) {
                        t.message?.let { Log.e("onFailure error", it) }
                    }
                })
                for (i in notes.indices) {
                    if (userNameText.text.equals(notes[i].Username) && passwordText.text.equals(
                            notes[i].Password
                        )
                    ) {
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
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })


    }
}