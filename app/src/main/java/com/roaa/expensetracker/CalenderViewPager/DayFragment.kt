package com.roaa.expensetracker.CalenderViewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Utilities.toLocalDate
import com.roaa.expensetracker.Utilities.toNormalString
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.databinding.FragmentDayBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class DayFragment() : Fragment() {

    lateinit var binding: FragmentDayBinding
    val viewModel: TransactionsViewModel by viewModels()
    var position: Int = 0
    lateinit var date: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = arguments
        position = (args?.getString("position") ?: "0").toInt()
        date = (args?.getString("date") ?: LocalDate.now().toNormalString()).toLocalDate()

        binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setTransactionRecyclerView()
        setUpComposeRecyclerView()
    }

    fun setUpComposeRecyclerView() {
        binding.composeViewDayListSingleDayFragment.setContent {
            ExpenseTrackerTheme {
               // FragmentDayScreen(true, date)
            }
        }
    }


//    private fun setTransactionRecyclerView() {
//        binding.testRecyclerView.setLayoutManager(
//            LinearLayoutManager(
//                requireActivity()
//            )
//        )
//        viewModel.allTransactions.onEach {
//            val transactionViewAdapter = TransactionViewAdapter(
//                requireActivity(),
//                it
//            )
//            binding.testRecyclerView.adapter = transactionViewAdapter
//        }
//    }

}