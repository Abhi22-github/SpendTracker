package com.example.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.expensetracker.Adapters.TransactionViewAdapter;
import com.example.expensetracker.CalenderViewPager.CalenderViewPagerAdapter;
import com.example.expensetracker.Events.EventMessage;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.Model.TransactionClass;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.expensetracker.databinding.ActivityMainBinding;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private FloatingActionButton fabAddExpense;
    private RecyclerView recyclerViewTransactions;
    private Context context;
    private TransactionViewAdapter transactionViewAdapter;
    private AddActivityViewModel viewModel;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private NestedScrollView nestedScrollView;
    private LinearLayout linearLayoutEmptyScreenLayout;

    private ViewPager viewPagerCalender;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);
        viewModel.initializeDatabaseRepository(getApplication());

        // method to initialize vars
        initVars();

        // method to initialize views
        initViews();

        fabAddExpense.setOnClickListener(v -> sendUserToAddActivity());

        toolbar.setNavigationOnClickListener(v -> drawerLayout.open());

        navigationView.setNavigationItemSelectedListener(v -> sideMenuItemClickHandler(v));

        //setting up transaction recycler view
        setTransactionRecyclerView();

        //setting up calender month view pager
        setCalenderMonthViewPager();

        oneTimeOperationMethod();


    }

    private void setTransactionRecyclerView() {
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getAllTransactions().observe(this, new Observer<List<TransactionClass>>() {
            @Override
            public void onChanged(List<TransactionClass> transactionClasses) {
                transactionViewAdapter = new TransactionViewAdapter(MainActivity.this, viewModel.getAllTransactions().getValue());
                recyclerViewTransactions.setAdapter(transactionViewAdapter);
            }
        });
    }


    private void setCalenderMonthViewPager() {
        CalenderViewPagerAdapter calenderViewPagerAdapter = new CalenderViewPagerAdapter(this);
        viewPagerCalender.setAdapter(calenderViewPagerAdapter);
        viewPagerCalender.setCurrentItem(250, true);
    }

    private boolean sideMenuItemClickHandler(MenuItem v) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int menuID = v.getItemId();
                if (menuID == R.id.item_listView_sideNavigation) {
                    recyclerViewTransactions.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);
                } else if (menuID == R.id.item_monthView_sideNavigation) {
                    recyclerViewTransactions.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);

                } else if (menuID == R.id.item_expenseCategory_sideNavigation) {
                    sendUserToExpenseCategoryActivity();
                    navigationView.setCheckedItem(R.id.item_listView_sideNavigation);
                } else if (menuID == R.id.item_incomeCategory_sideNavigation) {
                    sendUserToIncomeCategoryActivity();
                    navigationView.setCheckedItem(R.id.item_listView_sideNavigation);
                } else if (menuID == R.id.item_settings_sideNavigation) {
                    navigationView.setCheckedItem(R.id.item_listView_sideNavigation);
                } else if (menuID == R.id.item_analyze_sideNavigation) {
                    navigationView.setCheckedItem(R.id.item_listView_sideNavigation);
                } else if (menuID == R.id.item_bankAccounts_sideNavigation) {
                    Toast.makeText(context, "manageBankAccounts", Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        });
        drawerLayout.close();
        return true;
    }

    private void initVars() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(Constants.sharedPreferencesName, MODE_PRIVATE);
    }


    private void initViews() {
        fabAddExpense = findViewById(R.id.fab_add_activity_main);
        recyclerViewTransactions = findViewById(R.id.recyclerView_transaction_mainActivity);
        appBarLayout = findViewById(R.id.appBarLayout_appBar_mainActivity);
        drawerLayout = findViewById(R.id.drawerLayout_drawer_mainActivity);
        toolbar = findViewById(R.id.toolbar_mainToolbar_mainActivity);
        navigationView = findViewById(R.id.navigationView_navigationContent_mainActivity);
        nestedScrollView = findViewById(R.id.nestedScrollView_scrollView_mainActivity);
        nestedScrollView.setFillViewport(true);
        viewPagerCalender = findViewById(R.id.viewPager_calender_mainActivity);
        linearLayoutEmptyScreenLayout = findViewById(R.id.linearLayout_emptyScreenDialog_mainActivity);
    }

    private void oneTimeOperationMethod() {
        int check = sharedPreferences.getInt(Constants.sharedPreferenceOneTimeCheckKey, 0);
        if (check == 0) {
            ArrayList<CategoryClass> categoryClassesList = new ArrayList<>();
            String[] expenseArray = getResources().getStringArray(R.array.expense_categories);

            for (String s : expenseArray) {
                CategoryClass categoryClassObject = new CategoryClass();
                categoryClassObject.setCategoryName(s);
                categoryClassObject.setCategoryType(Constants.expense);
                categoryClassObject.setCategoryIconNumber(1);
                categoryClassObject.setCategoryColorNumber(1);
                categoryClassesList.add(categoryClassObject);
            }

            String[] incomeArray = getResources().getStringArray(R.array.income_categories);

            for (String s : incomeArray) {
                CategoryClass categoryClassObject = new CategoryClass();
                categoryClassObject.setCategoryName(s);
                categoryClassObject.setCategoryType(Constants.income);
                categoryClassObject.setCategoryIconNumber(1);
                categoryClassObject.setCategoryColorNumber(1);
                categoryClassesList.add(categoryClassObject);
            }

            Log.d("Chips", "performed again");
            viewModel.fillCategoriesInDatabase(categoryClassesList);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.sharedPreferenceOneTimeCheckKey, 1);
            editor.apply();
        }
    }

    private void sendUserToAddActivity() {
        Intent intent = new Intent(context, AddActivity.class);
        startActivity(intent);
    }

    private void sendUserToExpenseCategoryActivity() {
        Intent intent = new Intent(context, ExpenseCategoryActivity.class);
        startActivity(intent);
        MainActivity.this.overridePendingTransition(android.R.anim.fade_in, 0);
    }

    private void sendUserToIncomeCategoryActivity() {
        Intent intent = new Intent(context, IncomeCategoryActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void EventHandler(EventMessage eventMessage) {
        Log.d("Chips", "completed again2");
        if (eventMessage.getEventCode() == 9) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.sharedPreferenceOneTimeCheckKey, 1);
            editor.apply();
            Log.d("Chips", "completed again3");
        }
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

