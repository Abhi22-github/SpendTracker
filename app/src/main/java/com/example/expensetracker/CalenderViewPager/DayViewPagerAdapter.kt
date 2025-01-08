package com.example.expensetracker.CalenderViewPager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.ViewModels.AddActivityViewModel
import java.time.LocalDate

class DayViewPagerAdapter(
    val context: Context,
    val viewModel: AddActivityViewModel,
) :
    PagerAdapter() {
    val MAX_VALUE = 500
    lateinit var viewContainer: ViewGroup

    override fun getCount(): Int {
        return MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.getAllTransactionsForDate(getDateByPagerPosition(position).toString().replace("-","").toLong()).observe(context as LifecycleOwner, Observer {
            val adapter: OnlyDayAdapter = object : OnlyDayAdapter(context,it) {
                override fun onBindViewHolder(holder: RecyclerView.ViewHolder,transactionClasses: MutableList<TransactionClass>,position: Int) {
                    this@DayViewPagerAdapter.onBindView(holder.itemView,transactionClasses,position)
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object :
                        RecyclerView.ViewHolder(this@DayViewPagerAdapter.onCreateView(parent)) {}
                }
            }
            recyclerView.adapter = adapter
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

    private fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.single_transaction_view_holder, parent, false)
        return view
    }

    private fun onBindView(itemView: View,transactionClasses: MutableList<TransactionClass>, position: Int) {
        val main = itemView.findViewById<LinearLayout>(R.id.main)
        val note = itemView.findViewById<TextView>(R.id.textView_note_transactionViewHolder)
        val amount = itemView.findViewById<TextView>(R.id.textView_amount_transactionViewHolder)
        val body =
            itemView.findViewById<RelativeLayout>(R.id.relativeLayout_singleEntry_transactionViewHolder)
        val day = itemView.findViewById<TextView>(R.id.textView_day_transactionViewHolder)
        val date = itemView.findViewById<TextView>(R.id.textView_date_transactionViewHolder)
        val dayDateView =
            itemView.findViewById<LinearLayout>(R.id.linearLayout_dateDay_transactionViewHolder)
        val circleBackground =
            itemView.findViewById<RelativeLayout>(R.id.relativeLayout_circleBackground_transactionViewHolder)


        if(transactionClasses.size != 0){
            try{
                amount.text = transactionClasses.get(position).amount.toString()
            }catch (e:Exception){

            }

        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun getDateByPagerPosition(position: Int):LocalDate{
        val date = LocalDate.now()
        return date.plusDays(position.toLong()-250)
    }


}