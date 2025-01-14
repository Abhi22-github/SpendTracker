package com.example.expensetracker.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.Adapters.TransactionViewAdapter
import com.example.expensetracker.Composables.ExpenseTrackerTheme
import com.example.expensetracker.Composables.components.TransactionsListCompose
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.Utilities.getFirstAndLastDateOfGivenPeriod
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentListViewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate


@AndroidEntryPoint
class ListViewFragment : Fragment() {
    private lateinit var binding: FragmentListViewBinding
    private val viewModel: AddActivityViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)

        setTransactionRecyclerView()

        setUpTotalExpenseAndIncomeAmount()

        binding.composeViewTransactionsListFragment.setContent {
            ExpenseTrackerTheme {
                Surface {
                    TransactionsListCompose(false, LocalDate.now())
                }
            }

        }
    }

    fun setUpTotalExpenseAndIncomeAmount() {

        val currentDate = java.time.LocalDate.now()
        val firstDate = currentDate.withDayOfMonth(1)
        val lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

        val (firstDay, lastDay) = getFirstAndLastDateOfGivenPeriod(firstDate, lastDate)
        viewModel.getTotalIncomeForRange(firstDay, lastDay).observe(viewLifecycleOwner, Observer {
            binding.textViewIncomeListViewFragment.text = "₹" + parseAmount(it.totalAmount)
        })
        viewModel.getTotalExpenseForRange(firstDay, lastDay).observe(viewLifecycleOwner, Observer {
            binding.textViewExpenseListViewFragment.text = "₹" + parseAmount(it.totalAmount)
        })
    }

    private fun setTransactionRecyclerView() {
        binding.recyclerViewTransactionMainActivity.setLayoutManager(
            LinearLayoutManager(
                requireActivity()
            )
        )
        viewModel.allTransactions.observe(viewLifecycleOwner, Observer<List<TransactionClass>> {
            if (it.size == 0) {
                binding.linearLayoutEmptyScreenDialogListView.visibility = View.VISIBLE
            } else {
                binding.linearLayoutEmptyScreenDialogListView.visibility = View.GONE
                val transactionViewAdapter = TransactionViewAdapter(
                    requireActivity(),
                    it
                )
                binding.recyclerViewTransactionMainActivity.adapter = transactionViewAdapter
            }

        })
    }
}