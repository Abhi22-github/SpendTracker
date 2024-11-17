package com.example.expensetracker;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.Events.EventMessage;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AddCategoryBottomSheet extends BottomSheetDialogFragment {
    private ImageButton cancelButton;
    private TextInputLayout textInputLayoutName;
    private TextInputEditText editTextName;
    private MaterialButton materialButtonCreateButton;
    private Chip chipExpense, chipIncome;
    private TextView textViewCategoryError;

    private View view;
    private AddActivityViewModel viewModel;
    private Integer categoryTypeFromActivity = 0;

    /*
    Expense -> 2
    Income -> 1
    both  -> 0
     */
    public AddCategoryBottomSheet(String type) {
        if (type.equals(Constants.expense)) {
            categoryTypeFromActivity = 2;
        } else if (type.equals(Constants.income)) {
            categoryTypeFromActivity = 1;
        } else {
            categoryTypeFromActivity = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_category_bottom_sheet_layout, container, false);

        //initializing the view model
        viewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);
        viewModel.initializeDatabaseRepository(getActivity().getApplication());

        //to initialize the view
        initView();

        //validate the filled data when create button clicked
        materialButtonCreateButton.setOnClickListener(v -> validateData());

        cancelButton.setOnClickListener(v -> dismiss());

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayoutName.setError("");

            }
        });

        /*
        we will disable functionality to choose between expense and income based on the activity
        from where the bottom sheet is called
         */
        categoryChipsStateManage();


        return view;
    }

    private void initView() {
        cancelButton = view.findViewById(R.id.imageButton_cancel_bottomSheet);
        textInputLayoutName = view.findViewById(R.id.editTextLayout_name_bottomSheet);
        editTextName = view.findViewById(R.id.editText_name_bottomSheet);
        materialButtonCreateButton = view.findViewById(R.id.button_saveButton_bottomSheet);

        chipExpense = view.findViewById(R.id.chip_expense_bottomSheet);
        chipIncome = view.findViewById(R.id.chip_income_bottomSheet);
        textViewCategoryError = view.findViewById(R.id.textview_categoryError_bottomSheet);
    }

    private void validateData() {
        String categoryName = editTextName.getText().toString();
        String categoryType = "";
        if (chipExpense.isChecked()) {
            categoryType = "";
            categoryType = Constants.expense;
        } else if (chipIncome.isChecked()) {
            categoryType = Constants.income;
        }
        viewModel.validateCategoryData(categoryName, categoryType);
    }

    @Subscribe
    public void EventBusHandler(EventMessage eventMessage) {
        /*
         codes
           1 -> error (category name provided is empty)
           2 -> error (category name provided length < 3)
           3 -> success (dismiss the bottom sheet)
         */
        if (eventMessage.getEventCode() == 1 || eventMessage.getEventCode() == 12) {
            textInputLayoutName.setError(eventMessage.getMessage());
        }
        if (eventMessage.getEventCode() == 2) {
            textViewCategoryError.setText(eventMessage.getMessage());
            textViewCategoryError.setVisibility(View.VISIBLE);
        }
        if (eventMessage.getEventCode() == 3) {
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void categoryChipsStateManage() {
        if (categoryTypeFromActivity == 2) {
            chipIncome.setEnabled(false);
            chipExpense.setChecked(true);
        } else if (categoryTypeFromActivity == 1) {
            chipExpense.setEnabled(false);
            chipIncome.setChecked(true);
        } else {
            chipExpense.setEnabled(true);
            chipExpense.setChecked(true);
            chipIncome.setEnabled(true);
        }
    }
}
