package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.CalenderViewPager.CalenderViewPagerAdapter
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentMonthViewBinding

class MonthViewFragment : Fragment() {
    private lateinit var binding: FragmentMonthViewBinding
    private lateinit var viewModel: AddActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMonthViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get<AddActivityViewModel>(
            AddActivityViewModel::class.java
        )
        viewModel.initializeDatabaseRepository(requireActivity().application)
        binding.nestedScrollViewScrollViewMonthFragment.isFillViewport = true

        setCalenderMonthViewPager()

    }

    private fun setCalenderMonthViewPager() {
        val calenderViewPagerAdapter = CalenderViewPagerAdapter(context, viewModel)
        binding.viewPagerCalenderMonthFragment.setAdapter(calenderViewPagerAdapter)
        binding.viewPagerCalenderMonthFragment.setCurrentItem(250, true)
    }


}