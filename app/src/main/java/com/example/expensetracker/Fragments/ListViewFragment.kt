package com.example.expensetracker.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.Adapters.TransactionViewAdapter
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.Utilities.getFirstAndLastDateOfGivenPeriod
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentListViewBinding


class ListViewFragment : Fragment() {
    private lateinit var binding: FragmentListViewBinding
    private lateinit var viewModel: AddActivityViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get<AddActivityViewModel>(
            AddActivityViewModel::class.java
        )
        viewModel.initializeDatabaseRepository(requireActivity().application)

        setTransactionRecyclerView()

        setUpTotalExpenseAndIncomeAmount()
    }

    fun setUpTotalExpenseAndIncomeAmount() {

        val currentDate = java.time.LocalDate.now()
        val firstDate = currentDate.withDayOfMonth(1)
        val lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

        val (firstDay,lastDay) = getFirstAndLastDateOfGivenPeriod(firstDate,lastDate)
        viewModel.getTotalIncomeForRange(firstDay,lastDay).observe(viewLifecycleOwner,Observer{
            binding.textViewIncomeListViewFragment.text = "₹"+parseAmount(it.totalAmount)
        })
        viewModel.getTotalExpenseForRange(firstDay,lastDay).observe(viewLifecycleOwner,Observer{
            binding.textViewExpenseListViewFragment.text = "₹"+parseAmount(it.totalAmount)
        })
    }

    private fun setTransactionRecyclerView() {
        binding.recyclerViewTransactionMainActivity.setLayoutManager(LinearLayoutManager(requireActivity()))
        viewModel.allTransactions.observe(viewLifecycleOwner,Observer<List<TransactionClass>>{
            if(it.size == 0){
                binding.linearLayoutEmptyScreenDialogListView.visibility = View.VISIBLE
            }else{
                binding.linearLayoutEmptyScreenDialogListView.visibility = View.GONE
                val transactionViewAdapter = TransactionViewAdapter(requireActivity(),
                    it)
                binding.recyclerViewTransactionMainActivity.adapter = transactionViewAdapter
            }

        })
    }
}