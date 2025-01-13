package com.example.expensetracker.Activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Adapters.CategoryExpenseViewChipAdapter
import com.example.expensetracker.Adapters.CategoryIncomeViewChipAdapter
import com.example.expensetracker.Events.EventMessage
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants
import com.example.expensetracker.Utilities.parseAmount
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class AddActivity : AppCompatActivity(), View.OnClickListener {
    // views
    private lateinit var editTextAmount: TextInputEditText
    private lateinit var editTextAddNote: TextInputEditText
    private lateinit var editTextDate: TextInputEditText
    private lateinit var imageButtonBackButton: ImageButton
    private lateinit var recyclerViewLayoutCategoryExpense: RecyclerView
    private lateinit var recyclerViewLayoutCategoryIncome: RecyclerView
    private lateinit var expenseButton: MaterialButton
    private lateinit var incomeButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var mainLayout: RelativeLayout
    private lateinit var categoryExpenseViewChipAdapter: CategoryExpenseViewChipAdapter
    private lateinit var categoryIncomeViewChipAdapter: CategoryIncomeViewChipAdapter
    private lateinit var toggleGroupCategory: MaterialButtonToggleGroup


    //vars
    private lateinit var chipsList: List<Chip>
    private val viewModel: AddActivityViewModel by viewModels()
    private lateinit var mContext: Context
    lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var categoryClassList: List<CategoryClass>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initVars()

        initView()


        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                editTextAmount.removeTextChangedListener(this)

                try {
                    var givenstring = s.toString()
                    var longval: Long
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replace(",".toRegex(), "")
                    }
                    val amount = parseAmount(givenstring.toLong())
                    editTextAmount.setText(amount)
                    editTextAmount.text?.let { editTextAmount.setSelection(it.length) }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                editTextAmount.addTextChangedListener(this)
            }
        })

        expenseButton.isChecked = true

        recyclerViewLayoutCategoryExpense.visibility = View.VISIBLE
        recyclerViewLayoutCategoryIncome.visibility = View.GONE

        toggleGroupCategory.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (checkedId == R.id.toggleButton_expenseButton_addActivity && isChecked) {
                Log.d("ToggleGroup", "Expense")
                viewModel.setTransactionTypeInViewModel(Constants.EXPENSE)
                recyclerViewLayoutCategoryExpense.visibility = View.VISIBLE
                recyclerViewLayoutCategoryIncome.visibility = View.GONE
            } else if (checkedId == R.id.toggleButton_incomeButton_addActivity && isChecked) {
                Log.d("ToggleGroup", "Income")
                viewModel.setTransactionTypeInViewModel(Constants.INCOME)
                recyclerViewLayoutCategoryExpense.visibility = View.GONE
                recyclerViewLayoutCategoryIncome.visibility = View.VISIBLE
            }
        }

        setUpCategoryRecyclerView()


        imageButtonBackButton.setOnClickListener { v: View? -> onBackPressedDispatcher.onBackPressed() }

        editTextDate.setOnClickListener { v: View? -> openCalender() }

        saveButton.setOnClickListener(this)

        editTextAmount.requestFocus()

        viewModel.onlyExpenseCategoryNames.observe(
            this,
            object : Observer<List<CategoryClass>> {
                override fun onChanged(value: List<CategoryClass>) {
                    categoryExpenseViewChipAdapter =
                        CategoryExpenseViewChipAdapter(this@AddActivity, value)
                    recyclerViewLayoutCategoryExpense.adapter = categoryExpenseViewChipAdapter
                }
            })

        viewModel.onlyIncomeCategoryNames.observe(this, object : Observer<List<CategoryClass>> {
            override fun onChanged(value: List<CategoryClass>) {
                categoryIncomeViewChipAdapter =
                    CategoryIncomeViewChipAdapter(this@AddActivity, value)
                recyclerViewLayoutCategoryIncome.adapter = categoryIncomeViewChipAdapter
            }
        })
    }

    private fun initVars() {
        simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        chipsList = ArrayList()
        categoryClassList = ArrayList()
    }

    private fun initView() {
        editTextAmount = findViewById(R.id.inputEditText_amount_addActivity)
        editTextAmount.requestFocus()

        toggleGroupCategory = findViewById(R.id.toggleGroup_category_addActivity)

        editTextAddNote = findViewById(R.id.inputEditText_addNote_addActivity)
        imageButtonBackButton = findViewById(R.id.imageButton_back_addActivity)

        recyclerViewLayoutCategoryExpense =
            findViewById(R.id.recyclerView_categoryExpense_addActivity)
        recyclerViewLayoutCategoryIncome =
            findViewById(R.id.recyclerView_categoryIncome_addActivity)

        expenseButton = findViewById(R.id.toggleButton_expenseButton_addActivity)
        incomeButton = findViewById(R.id.toggleButton_incomeButton_addActivity)
        saveButton = findViewById(R.id.button_saveButton_addActivity)
        mainLayout = findViewById(R.id.main)

        mContext = applicationContext

        editTextDate = findViewById(R.id.textInputEditText_date_addActivity)
        //set the text view date
        editTextDate.setText(simpleDateFormat.format(Date(viewModel.selectedDate)))
    }

    private fun setUpCategoryRecyclerView() {
        val flexboxLayoutManagerExpense =
            FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        flexboxLayoutManagerExpense.justifyContent = JustifyContent.CENTER
        val flexboxLayoutManagerIncome =
            FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        flexboxLayoutManagerIncome.justifyContent = JustifyContent.CENTER
        recyclerViewLayoutCategoryExpense.layoutManager = flexboxLayoutManagerExpense
        recyclerViewLayoutCategoryIncome.layoutManager = flexboxLayoutManagerIncome
    }


    private fun openCalender() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("")
            .setSelection(viewModel.selectedDate)
            .build()

        datePicker.show(supportFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener { selection ->
            editTextDate.setText(simpleDateFormat.format(Date(selection)))
            viewModel.saveSelectedDate(selection)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_saveButton_addActivity) {
            var amount = ""
            if (!editTextAmount.text.toString().isEmpty()) amount =
                editTextAmount.text.toString().replace(",", "")

            val note = editTextAddNote.text.toString()
            viewModel.validateFormData(amount, note, mContext)
        }
    }

    @Subscribe
    fun EventHandler(eventMessage: EventMessage) {
        /*
         Codes
         4 -> Error (Amount can't be zero)
         5 -> Error (Category is not selected)
         6 -> success ( goto prev activity)
         7 -> get selected Expense category text from category recycler view and update it on view model
         8 -> get selected Income category text from category recycler view and update it on view model
         */
        if (eventMessage.getEventCode() == 4 || eventMessage.getEventCode() == 5) {
            showSnackBar(eventMessage.getMessage())
        }
        if (eventMessage.getEventCode() == 6) {
            onBackPressedDispatcher.onBackPressed()
        }
        if (eventMessage.getEventCode() == 7) {
            viewModel.storeCurrentExpenseSelectChip(eventMessage.getMessage())
        }
        if (eventMessage.getEventCode() == 8) {
            Log.d("Chips", "EventBUs" + eventMessage.getMessage())
            viewModel.storeCurrentIncomeSelectChip(eventMessage.getMessage())
        }
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}