package com.example.expensetracker.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.example.expensetracker.CalenderViewPager.testViewPager2Adapter
import com.example.expensetracker.Utilities.getPreviousAndNextDays
import com.example.expensetracker.Utilities.toLocalDate
import com.example.expensetracker.databinding.ActivityDayViewTestBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class DayViewActivityTest : AppCompatActivity() {
    lateinit var binding: ActivityDayViewTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayViewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCalenderDayViewPager()
    }

    private fun setCalenderDayViewPager() {
        val start = System.currentTimeMillis()
        val date = LocalDate.now()
        var dateList = getPreviousAndNextDays(date).toMutableList()
//        val onlyDayAdapter = context?.let { DayViewPagerAdapter(it, viewModel, dateList) }
//        binding.viewPagerCalenderDayFragment.setAdapter(onlyDayAdapter)
////        viewModel.currentSelectedDate.observe(context as LifecycleOwner, Observer {
////            binding.viewPagerCalenderDayFragment.setCurrentItem(250 - it, true)
////        })
//
//        binding.tabLayoutDayDayView.setupWithViewPager(binding.viewPagerCalenderDayFragment)
//
//        binding.viewPagerCalenderDayFragment.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                if (position == dateList.size - 5) {
//
//                    binding.viewPager2CalenderDayFragment.postDelayed(Runnable {
//                        dateList.addAll(getNext10Dates(dateList[dateList.size - 1].toLocalDate()))
//                        onlyDayAdapter?.notifyDataSetChanged()
//                    }, 300)
//                }
//                if (position == 5) {
//
//                    binding.viewPager2CalenderDayFragment.postDelayed(Runnable {
//                        dateList.addAll(0, getPrev10Dates(dateList[0].toLocalDate()))
//                        // binding.viewPager2CalenderDayFragment.setCurrentItem(14,false)
//                        onlyDayAdapter?.notifyDataSetChanged()
//                    }, 300)
//
//                }
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//        })


//            val onlyDayAdapter2 = DayViewPager2Adapter(requireActivity(), viewModel, dateList)
//            binding.viewPager2CalenderDayFragment.setAdapter(onlyDayAdapter2)
//            viewModel.currentSelectedDate.observe(requireActivity() as LifecycleOwner, Observer {
//                Log.d("val - ", it.toString())
//                binding.viewPager2CalenderDayFragment.setCurrentItem(CURRENT_PAGE - it, false)
//            })
//
//            binding.viewPager2CalenderDayFragment.offscreenPageLimit = 4
//
//            binding.tabLayoutDayDayView.addOnTabSelectedListener(object :
//                TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    val onlyDate = tab?.view?.findViewById<TextView>(R.id.textView_date_tabItemView)
//                    val onlyDay = tab?.view?.findViewById<TextView>(R.id.textView_day_tabItemView)
//                    onlyDate?.setTextColor(resources.getColor(R.color.expense_orange))
//                    onlyDay?.setTextColor(resources.getColor(R.color.expense_orange))
//
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    val onlyDate = tab?.view?.findViewById<TextView>(R.id.textView_date_tabItemView)
//                    val onlyDay = tab?.view?.findViewById<TextView>(R.id.textView_day_tabItemView)
//                    onlyDate?.setTextColor(resources.getColor(R.color.black))
//                    onlyDay?.setTextColor(resources.getColor(R.color.black))
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//
//                }
//
//            })
//
//
//            TabLayoutMediator(
//                binding.tabLayoutDayDayView, binding.viewPager2CalenderDayFragment
//            ) { tab, position ->
//                if (onlyDayAdapter2 != null) {
//                    tab.customView = layoutInflater.inflate(R.layout.tab_item_view, null)
//                    val onlyDate =
//                        tab.view.findViewById<TextView>(R.id.textView_date_tabItemView)
//                    onlyDate.text = onlyDayAdapter2.getOnlyDateByPagerPosition(position)
//                    val onlyDay = tab.view.findViewById<TextView>(R.id.textView_day_tabItemView)
//                    onlyDay.text = onlyDayAdapter2.getDayByPagerPosition(position)
//                }
//
//            }.attach()


        val onlyDayAdapter2Frag =
            testViewPager2Adapter(
                supportFragmentManager,
                lifecycle,
                dateList,
                binding.viewPager2CalenderDayFragment
            )

        onlyDayAdapter2Frag.setData(dateList)
//        binding.viewPager2CalenderDayFragment.isSaveFromParentEnabled = false
        binding.viewPager2CalenderDayFragment.setAdapter(onlyDayAdapter2Frag)
//        viewModel.currentSelectedDate.observe(requireActivity() as LifecycleOwner, Observer {
//            binding.viewPager2CalenderDayFragment.setCurrentItem(CURRENT_PAGE - it, false)
//        })


        val tabMediator = TabLayoutMediator(
            binding.tabLayoutDayDayView, binding.viewPager2CalenderDayFragment
        ) { tab, position ->
            val split = dateList[position].split("-")
            tab.text = split[2]
        }

        tabMediator.attach()

//        GlobalScope.launch {
//            Log.d("Hello", "Reached")
//            dateList = getPreviousAndNextDays(LocalDate.now())
//            onlyDayAdapter2Frag.setData(dateList)
//            binding.viewPager2CalenderDayFragment.post(kotlinx.coroutines.Runnable {
//                dateList = getPreviousAndNextDays(LocalDate.now())
//                binding.viewPager2CalenderDayFragment.setCurrentItem(dateList.size / 2, false)
//                onlyDayAdapter2Frag.notifyDataSetChanged()
//            })
//
//        }


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
        Log.d("frag", "total time for Activity ${end - start}")


    }
}