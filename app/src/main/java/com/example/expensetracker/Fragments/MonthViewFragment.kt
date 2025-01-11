package com.example.expensetracker.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.expensetracker.CalenderViewPager.CalenderViewPagerAdapter
import com.example.expensetracker.Utilities.Constants.CURRENT_PAGE
import com.example.expensetracker.Utilities.getFirstAndLastDateOfGivenPeriod
import com.example.expensetracker.Utilities.launchCoroutine
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentMonthViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager

@AndroidEntryPoint
class MonthViewFragment : Fragment() {
    private lateinit var binding: FragmentMonthViewBinding
    private val viewModel: AddActivityViewModel by activityViewModels()

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

      //  viewModel = ViewModelProvider(requireActivity()).get(AddActivityViewModel::class.java)

        binding.nestedScrollViewScrollViewMonthFragment.isFillViewport = true

        setCalenderMonthViewPager()
        setUpTotalExpenseAndIncomeAmount()
    }

    fun setUpTotalExpenseAndIncomeAmount() {
        val currentDate = java.time.LocalDate.now()
        val firstDate = currentDate.withDayOfMonth(1)
        val lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

        val (firstDay,lastDay) = getFirstAndLastDateOfGivenPeriod(firstDate,lastDate)
        viewModel.getTotalIncomeForRange(firstDay,lastDay).observe(viewLifecycleOwner, Observer{
            binding.textViewIncomeMonthViewFragment.text = "₹"+parseAmount(it.totalAmount)
        })
        viewModel.getTotalExpenseForRange(firstDay,lastDay).observe(viewLifecycleOwner,Observer{
            binding.textViewExpenseMonthViewFragment.text = "₹"+parseAmount(it.totalAmount)
        })
    }

    private fun setCalenderMonthViewPager() {
        val mContext: Activity = FragmentComponentManager.findActivity(context) as Activity
        val calenderViewPagerAdapter = CalenderViewPagerAdapter(mContext, viewModel)
        binding.viewPagerCalenderMonthFragment.setAdapter(calenderViewPagerAdapter)
        launchCoroutine(binding.viewPagerCalenderMonthFragment.setCurrentItem(CURRENT_PAGE, false))

    }


}