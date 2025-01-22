package com.roaa.expensetracker.CalenderViewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class DayViewPagerAdapter(
    fragment: FragmentManager, lifecycle : Lifecycle, val dateList: MutableList<String>, val viewPager: ViewPager2
) : FragmentStateAdapter(fragment,lifecycle) {
    var list = mutableListOf<String>()
    fun setData(list1:MutableList<String>){
        list = list1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment =  DayFragment()
        val args = Bundle()
        args.putString("position", position.toString())
        args.putString("date",list[position])
        fragment.arguments = args
        return fragment

    }



}