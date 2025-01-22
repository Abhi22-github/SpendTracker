package com.roaa.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roaa.expensetracker.Model.TransactionClass
import com.roaa.expensetracker.Utilities.Constants
import com.roaa.expensetracker.Utilities.getDateFromMillis
import com.roaa.expensetracker.Utilities.parseAmount
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.databinding.FragmentTransactionDetailBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDetailBottomSheet(val transactionClass: TransactionClass) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTransactionDetailBottomSheetBinding
    private val viewModel: TransactionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPage()
        binding.materialButtonEditTransactionDetails.setOnClickListener {
            sendUserToEditPageActivity()
        }
        binding.materialButtonDeleteTransactionDetails.setOnClickListener {
            deleteCurrentEntryFromDatabase()
        }
    }
    private fun setUpPage(){
        binding.chipCategoryTransactionDetails.text = transactionClass.category
        binding.textViewDateTransactionDetails.text = getDateFromMillis(transactionClass.dateWithTime)
        binding.textViewAmountTransactionDetails.text = "₹"+parseAmount(transactionClass.amount)+".00"
        binding.textViewNoteTransactionDetails.text = transactionClass.note
        if(transactionClass.type.equals(Constants.INCOME)){
            binding.imageViewTypeTransactionDetails.setImageDrawable(resources.getDrawable(R.drawable.icon_income))
            binding.textViewAmountTransactionDetails.setTextColor(resources.getColor(R.color.green))
        }else if(transactionClass.type.equals(Constants.EXPENSE)){
            binding.imageViewTypeTransactionDetails.setImageDrawable(resources.getDrawable(R.drawable.icon_expense))
            binding.textViewAmountTransactionDetails.setTextColor(resources.getColor(R.color.expense_orange))
        }
    }

    private fun sendUserToEditPageActivity(){

    }

    private fun deleteCurrentEntryFromDatabase(){
        viewModel.deleteSingleTransaction(transactionClass)
        dismiss()
    }

}