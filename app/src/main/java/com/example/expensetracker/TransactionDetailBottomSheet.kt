package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Model.TransactionClass
import com.example.expensetracker.Utilities.Constants
import com.example.expensetracker.Utilities.getDateFromMillis
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.FragmentTransactionDetailBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TransactionDetailBottomSheet(val transactionClass: TransactionClass) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTransactionDetailBottomSheetBinding
    private lateinit var viewModel:AddActivityViewModel

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

        //initializing the view model
        viewModel =
            ViewModelProvider(this).get<AddActivityViewModel>(AddActivityViewModel::class.java)
        viewModel.initializeDatabaseRepository(requireActivity().application)

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
        binding.textViewAmountTransactionDetails.text = "Rs "+parseAmount(transactionClass.amount)+".00"
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