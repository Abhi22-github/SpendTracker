package com.example.expensetracker.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.CalenderViewPager.DayViewPager2Adapter
import com.example.expensetracker.CalenderViewPager.DayViewPagerAdapter
import com.example.expensetracker.R
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentDayViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DayViewFragment : Fragment() {
    private lateinit var binding: FragmentDayViewBinding
    private lateinit var viewModel: AddActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get<AddActivityViewModel>(
            AddActivityViewModel::class.java
        )
        viewModel.initializeDatabaseRepository(requireActivity().application)

        setCalenderDayViewPager()

    }

    private fun setCalenderDayViewPager() {
        val onlyDayAdapter = context?.let { DayViewPagerAdapter(it, viewModel) }
        binding.viewPagerCalenderDayFragment.setAdapter(onlyDayAdapter)
        viewModel.currentSelectedDate.observe(context as LifecycleOwner, Observer {
            binding.viewPagerCalenderDayFragment.setCurrentItem(250 - it, true)
        })

        binding.tabLayoutDayDayView.setupWithViewPager(binding.viewPagerCalenderDayFragment)

        val onlyDayAdapter2 = context?.let { DayViewPager2Adapter(it, viewModel) }
        binding.viewPager2CalenderDayFragment.setAdapter(onlyDayAdapter2)
        viewModel.currentSelectedDate.observe(context as LifecycleOwner, Observer {
            binding.viewPager2CalenderDayFragment.setCurrentItem(250 - it, false)
        })

        binding.tabLayoutDayDayView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val onlyDate = tab?.view?.findViewById<TextView>(R.id.textView_date_tabItemView)
                val onlyDay = tab?.view?.findViewById<TextView>(R.id.textView_day_tabItemView)
                onlyDate?.setTextColor(resources.getColor(R.color.expense_orange))
                onlyDay?.setTextColor(resources.getColor(R.color.expense_orange))

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val onlyDate = tab?.view?.findViewById<TextView>(R.id.textView_date_tabItemView)
                val onlyDay = tab?.view?.findViewById<TextView>(R.id.textView_day_tabItemView)
                onlyDate?.setTextColor(resources.getColor(R.color.black))
                onlyDay?.setTextColor(resources.getColor(R.color.black))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        TabLayoutMediator(
            binding.tabLayoutDayDayView,
            binding.viewPager2CalenderDayFragment
        ) { tab, position ->
            if (onlyDayAdapter2 != null) {
                tab.customView = layoutInflater.inflate(R.layout.tab_item_view, null)
                val onlyDate = tab.view.findViewById<TextView>(R.id.textView_date_tabItemView)
                onlyDate.text = onlyDayAdapter2.getOnlyDateByPagerPosition(position)
                val onlyDay = tab.view.findViewById<TextView>(R.id.textView_day_tabItemView)
                onlyDay.text = onlyDayAdapter2.getDayByPagerPosition(position)
            }

        }.attach()

    }

}