package com.example.expensetracker.CalenderViewPager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.expensetracker.Activity.MainActivity
import com.example.expensetracker.Fragments.DayViewFragment
import com.example.expensetracker.Model.Day
import com.example.expensetracker.Model.TotalExpenseIncomeClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants
import com.example.expensetracker.Utilities.LocalDateToLong
import com.example.expensetracker.Utilities.convertLocalDateToLong
import com.example.expensetracker.Utilities.convertTotalExpenseIncomeClassToMap
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Objects
import kotlin.math.abs


class CalenderViewPagerAdapter(
    private val context: Context,
    private val viewModel: AddActivityViewModel
) : PagerAdapter() {
    private val layoutInflater: LayoutInflater
    private lateinit var viewContainer: ViewGroup
    private val todayDate: LocalDate

    private lateinit var selectedDate: LocalDate
    private lateinit var daysList: ArrayList<LocalDate>
    private var map: HashMap<Long, Pair<Long, Long>>

    var dateTimeFormatter: DateTimeFormatter

    init {
        layoutInflater = LayoutInflater.from(context)
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        todayDate = LocalDate.now()
        map = HashMap()
    }

    override fun getCount(): Int {
        return Constants.MAX_PAGES
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //initializing recycler view

        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = GridLayoutManager(context, 7)


        //getting the current page month days in array
        daysList = daysInMonthArray(position)
        val daysOfMonthAdapter: DaysOfMonthAdapter =
            object : DaysOfMonthAdapter(context, daysList) {
                public override fun onBindViewHolder(
                    holder: RecyclerView.ViewHolder,
                    date: LocalDate,
                    map: HashMap<Long, Pair<Long, Long>>
                ) {
                    this@CalenderViewPagerAdapter.onBindView(holder.itemView, date, map)
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object : RecyclerView.ViewHolder(
                        this@CalenderViewPagerAdapter.onCreateView(
                            parent,
                            viewType
                        )
                    ) {
                    }
                }
            }
        recyclerView.adapter = daysOfMonthAdapter

        daysList = daysInMonthArray(position)
        viewModel.getListOfTotalAmountPerDayForRange(
            LocalDateToLong(daysList!![0]), LocalDateToLong(
                daysList!![daysList!!.size - 1]
            )
        ).observe(
            (context as LifecycleOwner), object : Observer<List<TotalExpenseIncomeClass>> {
                override fun onChanged(value: List<TotalExpenseIncomeClass>) {
                    map = convertTotalExpenseIncomeClassToMap(value)
                    daysOfMonthAdapter.updateData(map)
                    daysOfMonthAdapter.notifyDataSetChanged()
                }
            })

        container.addView(
            recyclerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        viewContainer = container

        return recyclerView
    }

    //setting up the view and it's height
    private fun onCreateView(parent: ViewGroup, viewType: Int): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.single_date_calender_view, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * .1666666).toInt()
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    fun onBindView(view: View, date: LocalDate, map: HashMap<Long, Pair<Long, Long>>) {
        val textViewDate = view.findViewById<TextView>(R.id.textView_date_singleDateViewHolder)
        val textViewExpenseAmount =
            view.findViewById<TextView>(R.id.textView_expenseAmount_singleDateViewHolder)
        val textViewIncomeAmount =
            view.findViewById<TextView>(R.id.textView_incomeAmount_singleDateViewHolder)
        val relativeLayoutDateCircle =
            view.findViewById<RelativeLayout>(R.id.relativeLayout_circleBackground_singleDateViewHolder)
        val relativeLayoutExpenseBox =
            view.findViewById<RelativeLayout>(R.id.relativeLayout_backgroundExpense_singleDateViewHolder)
        val relativeLayoutIncomeBox =
            view.findViewById<RelativeLayout>(R.id.relativeLayout_backgroundIncome_singleDateViewHolder)
        val itemLayout =
            view.findViewById<LinearLayout>(R.id.linearLayout_singleItem_singleDateViewHolder)
        val dateSplit =
            date.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if ((todayDate.toString() == date.toString())) {
            relativeLayoutDateCircle.background =
                context.resources.getDrawable(R.drawable.circle_background, context.theme)
            relativeLayoutDateCircle.background.setTint(
                context.resources.getColor(
                    R.color.black,
                    context.theme
                )
            )
            textViewDate.setTextColor(context.resources.getColor(R.color.white, context.theme))
        }
        textViewDate.text = dateSplit.get(2)
        if (map != null && map.containsKey(convertLocalDateToLong(date))) {
            if (Objects.requireNonNull<Pair<Long, Long>>(map[convertLocalDateToLong(date)]).first != 0L) {
                textViewExpenseAmount.text =
                    "-₹" + parseAmount(map.get(convertLocalDateToLong(date))!!.first)
                relativeLayoutExpenseBox.visibility = View.VISIBLE
            }
            if (Objects.requireNonNull<Pair<Long, Long>>(map[convertLocalDateToLong(date)]).second != 0L) {
                textViewIncomeAmount.text =
                    "+₹" + parseAmount(map.get(convertLocalDateToLong(date))!!.second)
                relativeLayoutIncomeBox.visibility = View.VISIBLE
            }
        }

        itemLayout.setOnClickListener(View.OnClickListener {
            try {
                viewModel.setCurrentSelectedDate(date)
                val fragmentTransaction =
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.frameLayout_fragment_mainActivity,
                    DayViewFragment()
                )
                fragmentTransaction.addToBackStack("DayDetails")
                fragmentTransaction.commit()
            } catch (e: ClassCastException) {
                Toast.makeText(context, "Can't get fragment Manager$e", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //calculate the days in month
    fun daysInMonthArray(position: Int): ArrayList<LocalDate> {
        var position = position
        position = abs((position - Constants.CURRENT_PAGE).toDouble())
            .toInt()

        val daysInMonthArray = ArrayList<LocalDate>()
        if (position > 0) {
            selectedDate = LocalDate.now().plusMonths(position.toLong()).withDayOfMonth(15)
        } else if (position < 0) {
            selectedDate = LocalDate.now().minusMonths(position.toLong()).withDayOfMonth(15)
        } else {
            selectedDate = LocalDate.now()
        }

        //finding the exact  same date in prev month and next month
        val prevMonthSameDate = selectedDate.minusMonths(1)
        val nextMonthSameDate = selectedDate.plusMonths(1)


        //getting the prev and next Month
        val currentMonth = YearMonth.from(selectedDate)
        val prevMonth = YearMonth.from(selectedDate).minusMonths(1)
        val nextMonth = YearMonth.from(selectedDate).plusMonths(1)

        //finding out the days in prev,present and next month
        val daysInPrevMonth = prevMonth.lengthOfMonth()
        val daysInMonth = currentMonth.lengthOfMonth()
        val daysInNextMonth = nextMonth.lengthOfMonth()


        //finding the firstDate of current month
        val firstOfMonth = selectedDate.withDayOfMonth(1)

        //getting the day of week for the 1st date of current month
        /*
        0 -> Sunday
        1 -> Monday
        2 -> Tuesday
        3 -> Wednesday
        4 -> Thursday
        5 -> Friday
        6 -> Saturday
         */
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        //loop to get 42 entries including some of prev months and some of next months with present month
        for (i in 0..41) {
            val day = Day()
            if (i < dayOfWeek) {
                //prev months condition
                //day.setDate(prevMonthSameDate.withDayOfMonth((Math.abs((i) - daysInPrevMonth))));
                day.date = prevMonthSameDate.withDayOfMonth(
                    (abs((daysInPrevMonth - abs((dayOfWeek - (i + 1)).toDouble())).toDouble())
                        .toInt())
                )
                daysInMonthArray.add(day.date)
            } else if (i >= daysInMonth + dayOfWeek) {
                //next month condition
                day.date = nextMonthSameDate.withDayOfMonth(
                    abs(((i + 1) - (daysInMonth + dayOfWeek)).toDouble())
                        .toInt()
                )
                daysInMonthArray.add(day.date)
            } else if (i < daysInMonth + dayOfWeek) {
                //current month condition
                day.date = selectedDate.withDayOfMonth((i + 1) - dayOfWeek)
                daysInMonthArray.add(day.date)
            }
        }
        return daysInMonthArray
    }
}




