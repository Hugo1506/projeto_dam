package com.example.projeto_dam

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> Fragmento1()
            1 -> Fragmento2()
            2 -> Fragmento3()
            3 -> Fragmento4()
            else -> Fragmento1()
        }
    }

}