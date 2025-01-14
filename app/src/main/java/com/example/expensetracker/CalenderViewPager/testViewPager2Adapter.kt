package com.example.expensetracker.CalenderViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class testViewPager2Adapter(
    fragment: Fragment, val dateList: MutableList<String>, val viewPager: ViewPager2
) : FragmentStateAdapter(fragment) {
    var list = mutableListOf<String>()
    fun setData(list1:MutableList<String>){
        list = list1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment =  testFragment()
        val args = Bundle()
        args.putString("position", position.toString())
        args.putString("date",list[position])
        fragment.arguments = args
        return fragment

    }



}