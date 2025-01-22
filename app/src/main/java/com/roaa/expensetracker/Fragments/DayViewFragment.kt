package com.roaa.expensetracker.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.google.android.material.tabs.TabLayoutMediator
import com.roaa.expensetracker.CalenderViewPager.DayViewPagerAdapter
import com.roaa.expensetracker.Utilities.getPreviousAndNextDays
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.databinding.FragmentDayViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class DayViewFragment : Fragment() {
    private lateinit var binding: FragmentDayViewBinding
    private val viewModel: TransactionsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDayViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCalenderDayViewPager()

    }

    private fun setCalenderDayViewPager() {
        val start = System.currentTimeMillis()
        val date = LocalDate.now()
        var dateList = getPreviousAndNextDays(date).toMutableList()

        val onlyDayAdapter2Frag =
            DayViewPagerAdapter(childFragmentManager,lifecycle, dateList, binding.viewPager2CalenderDayFragment)

        onlyDayAdapter2Frag.setData(dateList)

        binding.viewPager2CalenderDayFragment.setAdapter(onlyDayAdapter2Frag)

        val tabMediator = TabLayoutMediator(
            binding.tabLayoutDayDayView, binding.viewPager2CalenderDayFragment
        ) { tab, position ->
            val split = dateList[position].split("-")
            tab.text = split[2]
        }

        tabMediator.attach()

        binding.viewPager2CalenderDayFragment.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position < 5 || position >= dateList.size - 5 && binding.viewPager2CalenderDayFragment.scrollState == SCROLL_STATE_IDLE)
                    GlobalScope.launch {
                        val date = dateList[position]
                        dateList = getPreviousAndNextDays(date.toLocalDate())
                        onlyDayAdapter2Frag.setData(dateList)
                        binding.viewPager2CalenderDayFragment.post(kotlinx.coroutines.Runnable {
                            dateList = getPreviousAndNextDays(date.toLocalDate())
                            binding.viewPager2CalenderDayFragment.setCurrentItem(
                                dateList.size / 2, false
                            )
                            onlyDayAdapter2Frag.notifyDataSetChanged()
                        })

                    }
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })


        binding.viewPager2CalenderDayFragment.setCurrentItem(dateList.size / 2, false)

        val end = System.currentTimeMillis()
        Log.d("frag", "total time for frag ${end - start}")


    }

}