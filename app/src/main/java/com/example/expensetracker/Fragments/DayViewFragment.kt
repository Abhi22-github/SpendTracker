package com.example.expensetracker.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.CalenderViewPager.DayViewPagerAdapter
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentDayViewBinding


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
        binding.nestedScrollViewScrollViewDayFragment.isFillViewport = true

        setCalenderDayViewPager()

    }

    private fun setCalenderDayViewPager() {
        val onlyDayAdapter = context?.let { DayViewPagerAdapter(it, viewModel) }
        binding.viewPagerCalenderDayFragment.setAdapter(onlyDayAdapter)
        viewModel.currentSelectedDate.observe(context as LifecycleOwner, Observer {
            binding.viewPagerCalenderDayFragment.setCurrentItem(250-it, true)
        })

    }




}