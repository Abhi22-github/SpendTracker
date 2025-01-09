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
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.LocalDateToLong
import com.example.expensetracker.ViewModels.AddActivityViewModel
import java.time.LocalDate


class DayViewPager2Adapter(
    val context: Context,
    val viewModel: AddActivityViewModel,
) :
    RecyclerView.Adapter<DayViewPager2Adapter.ViewHolder>() {
    val MAX_VALUE = 500

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_day_layout, parent, false)
        return DayViewPager2Adapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return MAX_VALUE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.getAllTransactionsForDate(LocalDateToLong(getLocalDateByPagerPosition(position)))
            .observe(context as LifecycleOwner, Observer {
                if (it.size != 0) {
                    holder.emptyMessageView.visibility = View.GONE
                    holder.recyclerView.visibility = View.VISIBLE
                    val adapter: OnlyDayAdapter = object : OnlyDayAdapter(context, it) {
                        override fun onBindViewHolder(
                            holder: RecyclerView.ViewHolder,
                            transactionClasses: MutableList<TransactionClass>,
                            position: Int
                        ) {
                            this@DayViewPager2Adapter.onBindView(
                                holder.itemView,
                                transactionClasses,
                                position
                            )
                        }

                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): RecyclerView.ViewHolder {
                            return object :
                                RecyclerView.ViewHolder(
                                    this@DayViewPager2Adapter.onCreateView(
                                        parent
                                    )
                                ) {}
                        }
                    }
                    holder.recyclerView.adapter = adapter
                } else {
                    holder.emptyMessageView.visibility = View.VISIBLE
                    holder.recyclerView.visibility = View.GONE
                }
            })

    }

    private fun onCreateView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.single_transaction_view_holder, parent, false)
        return view
    }

    private fun onBindView(
        itemView: View,
        transactionClasses: MutableList<TransactionClass>,
        position: Int
    ) {
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


        if (transactionClasses.size != 0) {
            try {
                amount.text = transactionClasses.get(position).amount.toString()
            } catch (e: Exception) {

            }

        }
    }

    fun getLocalDateByPagerPosition(position: Int): LocalDate {
        val date = LocalDate.now()
        return date.plusDays(position.toLong() - 250)
    }

    fun getDayByPagerPosition(position: Int):String{
        val date = getLocalDateByPagerPosition(position)
        return date.dayOfWeek.toString().take(3).lowercase().replaceFirstChar { it.uppercase() }
    }

    fun getOnlyDateByPagerPosition(position: Int):String{
        val date = getLocalDateByPagerPosition(position)
        return date.toString().takeLast(2)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView =
            itemView.findViewById<RecyclerView>(R.id.recyclerView_dayTransaction_dayRecyclerView)
        val emptyMessageView = itemView.findViewById<View>(R.id.include_emptyScreenDialog_singleDayLayout)
    }

}