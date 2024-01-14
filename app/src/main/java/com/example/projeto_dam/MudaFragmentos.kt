package com.example.projeto_dam

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return Fragmento1()
            1 -> return Fragmento2()
            2 -> return Fragmento3()
            3 -> return Fragmento4()
            else -> return Fragmento1()
        }
    }

}