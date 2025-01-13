package com.example.expensetracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.expensetracker.Events.EventMessage
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.Utilities.Constants
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class AddCategoryBottomSheet(private val categoryClassFromActivity: CategoryClass) :
    BottomSheetDialogFragment() {
    private lateinit var cancelButton: ImageButton
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var materialButtonCreateButton: MaterialButton
    private lateinit var chipExpense: Chip
    private lateinit var chipIncome: Chip
    private lateinit var textViewCategoryError: TextView

    private lateinit var view: View
    private val viewModel: AddActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.add_category_bottom_sheet_layout, container, false)

        //to initialize the view
        initView()

        setUpViews()

        //validate the filled data when create button clicked
        materialButtonCreateButton.setOnClickListener { v: View? -> validateData() }

        cancelButton.setOnClickListener { v: View? -> dismiss() }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                textInputLayoutName.error = ""
            }
        })

        return view
    }

    private fun setUpViews() {
        if (categoryClassFromActivity.categoryName != null) {
            editTextName.setText(categoryClassFromActivity.categoryName)
        }
        /*
        we will disable functionality to choose between expense and income based on the activity
        from where the bottom sheet is called
         */
        categoryChipsStateManage()
    }

    private fun initView() {
        cancelButton = view.findViewById(R.id.imageButton_cancel_bottomSheet)
        textInputLayoutName = view.findViewById(R.id.editTextLayout_name_bottomSheet)
        editTextName = view.findViewById(R.id.editText_name_bottomSheet)
        materialButtonCreateButton = view.findViewById(R.id.button_saveButton_bottomSheet)

        chipExpense = view.findViewById(R.id.chip_expense_bottomSheet)
        chipIncome = view.findViewById(R.id.chip_income_bottomSheet)
        textViewCategoryError = view.findViewById(R.id.textview_categoryError_bottomSheet)
    }

    private fun validateData() {
        val categoryName = editTextName.text.toString()
        var categoryType = ""
        if (chipExpense.isChecked) {
            categoryType = Constants.EXPENSE
        } else if (chipIncome.isChecked) {
            categoryType = Constants.INCOME
        }
        categoryClassFromActivity.categoryName = categoryName
        categoryClassFromActivity.categoryType = categoryType
        categoryClassFromActivity.categoryIconNumber = 1
        categoryClassFromActivity.categoryColorNumber = 1
        viewModel.validateCategoryData(categoryClassFromActivity)
    }

    @Subscribe
    fun EventBusHandler(eventMessage: EventMessage) {
        /*
         codes
           1 -> error (category name provided is empty)
           2 -> error (category name provided length < 3)
           3 -> success (dismiss the bottom sheet)
         */
        if (eventMessage.getEventCode() == 1 || eventMessage.getEventCode() == 12) {
            textInputLayoutName.error = eventMessage.getMessage()
        }
        if (eventMessage.getEventCode() == 2) {
            textViewCategoryError.text = eventMessage.getMessage()
            textViewCategoryError.visibility = View.VISIBLE
        }
        if (eventMessage.getEventCode() == 3) {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun categoryChipsStateManage() {
        if (Constants.EXPENSE == categoryClassFromActivity.categoryType) {
            chipIncome.isEnabled = false
            chipExpense.isChecked = true
        } else if (Constants.INCOME == categoryClassFromActivity.categoryType) {
            chipExpense.isEnabled = false
            chipIncome.isChecked = true
        } else {
            chipExpense.isEnabled = true
            chipExpense.isChecked = true
            chipIncome.isEnabled = true
        }
    }
}
