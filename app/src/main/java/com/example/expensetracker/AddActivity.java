package com.example.expensetracker;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Adapters.CategoryExpenseViewChipAdapter;
import com.example.expensetracker.Adapters.CategoryIncomeViewChipAdapter;
import com.example.expensetracker.Events.EventMessage;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    // views
    private TextInputEditText editTextAmount, editTextAddNote, editTextDate;
    private ImageButton imageButtonBackButton, imageButtonCategoryButton;
    private RecyclerView recyclerViewLayoutCategoryExpense, recyclerViewLayoutCategoryIncome;
    private MaterialButton expenseButton, incomeButton, saveButton;
    private RelativeLayout mainLayout;
    private CategoryExpenseViewChipAdapter categoryExpenseViewChipAdapter;
    private CategoryIncomeViewChipAdapter categoryIncomeViewChipAdapter;
    private MaterialButtonToggleGroup toggleGroupCategory;


    //vars
    private String[] chipsName;
    private List<Chip> chipsList;
    private AddActivityViewModel viewModel;
    private Context mContext;
    SimpleDateFormat simpleDateFormat;
    private List<CategoryClass> categoryClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        viewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);
        viewModel.initializeDatabaseRepository(getApplication());

        initVars();

        initView();


        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextAmount.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("##,##,##,###");
                    String formattedString = formatter.format(longval);
                    editTextAmount.setText(formattedString);
                    editTextAmount.setSelection(editTextAmount.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                editTextAmount.addTextChangedListener(this);
            }
        });

        expenseButton.setChecked(true);

        recyclerViewLayoutCategoryExpense.setVisibility(View.VISIBLE);
        recyclerViewLayoutCategoryIncome.setVisibility(View.GONE);

        toggleGroupCategory.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.toggleButton_expenseButton_addActivity && isChecked) {
                    Log.d("ToggleGroup", "Expense");
                    viewModel.setTransactionTypeInViewModel(Constants.EXPENSE);
                    recyclerViewLayoutCategoryExpense.setVisibility(View.VISIBLE);
                    recyclerViewLayoutCategoryIncome.setVisibility(View.GONE);

                } else if (checkedId == R.id.toggleButton_incomeButton_addActivity && isChecked) {
                    Log.d("ToggleGroup", "Income");
                    viewModel.setTransactionTypeInViewModel(Constants.INCOME);
                    recyclerViewLayoutCategoryExpense.setVisibility(View.GONE);
                    recyclerViewLayoutCategoryIncome.setVisibility(View.VISIBLE);
                }
            }
        });

        setUpCategoryRecyclerView();


        imageButtonBackButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        editTextDate.setOnClickListener(v -> openCalender());

        saveButton.setOnClickListener(this);

        editTextAmount.requestFocus();

        imageButtonCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryBottomSheet addCategoryBottomSheet = new AddCategoryBottomSheet(new CategoryClass());
                addCategoryBottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            }
        });


        viewModel.getOnlyExpenseCategoryNames().observe(this, new Observer<List<CategoryClass>>() {
            @Override
            public void onChanged(List<CategoryClass> categoryClasses) {
                categoryExpenseViewChipAdapter = new CategoryExpenseViewChipAdapter(AddActivity.this, categoryClasses);
                recyclerViewLayoutCategoryExpense.setAdapter(categoryExpenseViewChipAdapter);
            }
        });

        viewModel.getOnlyIncomeCategoryNames().observe(this, new Observer<List<CategoryClass>>() {
            @Override
            public void onChanged(List<CategoryClass> categoryClasses) {
                categoryIncomeViewChipAdapter = new CategoryIncomeViewChipAdapter(AddActivity.this, categoryClasses);
                recyclerViewLayoutCategoryIncome.setAdapter(categoryIncomeViewChipAdapter);
            }
        });


    }

    private void initVars() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        chipsList = new ArrayList<>();
        categoryClassList = new ArrayList<>();
    }

    private void initView() {
        editTextAmount = findViewById(R.id.inputEditText_amount_addActivity);
        editTextAmount.requestFocus();

        toggleGroupCategory = findViewById(R.id.toggleGroup_category_addActivity);

        editTextAddNote = findViewById(R.id.inputEditText_addNote_addActivity);
        imageButtonBackButton = findViewById(R.id.imageButton_back_addActivity);

        recyclerViewLayoutCategoryExpense = findViewById(R.id.recyclerView_categoryExpense_addActivity);
        recyclerViewLayoutCategoryIncome = findViewById(R.id.recyclerView_categoryIncome_addActivity);

        expenseButton = findViewById(R.id.toggleButton_expenseButton_addActivity);
        incomeButton = findViewById(R.id.toggleButton_incomeButton_addActivity);
        saveButton = findViewById(R.id.button_saveButton_addActivity);
        mainLayout = findViewById(R.id.main);

        mContext = getApplicationContext();

        editTextDate = findViewById(R.id.textInputEditText_date_addActivity);
        //set the text view date
        editTextDate.setText(simpleDateFormat.format(new Date(viewModel.getSelectedDate())));

        imageButtonCategoryButton = findViewById(R.id.imageButton_category_addActivity);


    }

    private void setUpCategoryRecyclerView() {
        FlexboxLayoutManager flexboxLayoutManagerExpense = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManagerExpense.setJustifyContent(JustifyContent.CENTER);
        FlexboxLayoutManager flexboxLayoutManagerIncome = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManagerIncome.setJustifyContent(JustifyContent.CENTER);
        recyclerViewLayoutCategoryExpense.setLayoutManager(flexboxLayoutManagerExpense);
        recyclerViewLayoutCategoryIncome.setLayoutManager(flexboxLayoutManagerIncome);
    }


    private void openCalender() {

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("")
                .setSelection(viewModel.getSelectedDate())
                .build();

        datePicker.show(getSupportFragmentManager(), "tag");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                editTextDate.setText(simpleDateFormat.format(new Date(selection)));
                viewModel.saveSelectedDate(selection);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_saveButton_addActivity) {
            String amount = "";
            if (!editTextAmount.getText().toString().isEmpty())
                amount = editTextAmount.getText().toString();

            String note = editTextAddNote.getText().toString();
            viewModel.validateFormData(amount, note, mContext);

        }
    }

    @Subscribe
    public void EventHandler(EventMessage eventMessage) {
        /*
         Codes
         4 -> Error (Amount can't be zero)
         5 -> Error (Category is not selected)
         6 -> success ( goto prev activity)
         7 -> get selected Expense category text from category recycler view and update it on view model
         8 -> get selected Income category text from category recycler view and update it on view model
         */
        if (eventMessage.getEventCode() == 4 || eventMessage.getEventCode() == 5) {
            showSnackBar(eventMessage.getMessage());
        }
        if (eventMessage.getEventCode() == 6) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        if (eventMessage.getEventCode() == 7) {
            viewModel.storeCurrentExpenseSelectChip(eventMessage.getMessage());
        }
        if (eventMessage.getEventCode() == 8) {
            Log.d("Chips","EventBUs"+eventMessage.getMessage());
            viewModel.storeCurrentIncomeSelectChip(eventMessage.getMessage());
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}