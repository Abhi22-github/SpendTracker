package com.example.expensetracker.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import com.example.expensetracker.Adapters.TransactionViewAdapter;
import com.example.expensetracker.Events.EventMessage;
import com.example.expensetracker.Fragments.ListViewFragment;
import com.example.expensetracker.Fragments.MonthViewFragment;
import com.example.expensetracker.Model.CategoryClass;
import com.example.expensetracker.R;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private FloatingActionButton fabAddExpense;
    //private RecyclerView recyclerViewTransactions;
    private Context context;
    private TransactionViewAdapter transactionViewAdapter;
    private AddActivityViewModel viewModel;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private LinearLayout linearLayoutEmptyScreenLayout;

    private ViewPager viewPagerCalender;
    private SharedPreferences sharedPreferences;
    private FragmentTransaction fragmentTransaction;

    private ListViewFragment listViewFragment;
    private MonthViewFragment monthViewFragment;


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

        setUpTransactionFragment();

        oneTimeOperationMethod();


    }

    private void setUpTransactionFragment(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_fragment_mainActivity,listViewFragment);
                fragmentTransaction.commit();
            }
        });

    }

    private void setUpMonthFragment(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_fragment_mainActivity,monthViewFragment);
                fragmentTransaction.addToBackStack("MonthView");
                fragmentTransaction.commit();
            }
        });

    }

    private boolean sideMenuItemClickHandler(MenuItem v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int menuID = v.getItemId();
                if(menuID == R.id.item_listView_sideNavigation){
                    setUpTransactionFragment();
                    navigationView.setCheckedItem(R.id.item_listView_sideNavigation);
                }else if (menuID == R.id.item_monthView_sideNavigation) {
                    setUpMonthFragment();
                    navigationView.setCheckedItem(R.id.item_monthView_sideNavigation);
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
        },300);
        drawerLayout.close();
        return true;
    }

    private void initVars() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(Constants.sharedPreferencesName, MODE_PRIVATE);
        listViewFragment = new ListViewFragment();
        monthViewFragment = new MonthViewFragment();
    }


    private void initViews() {
        fabAddExpense = findViewById(R.id.fab_add_activity_main);
        appBarLayout = findViewById(R.id.appBarLayout_appBar_mainActivity);
        drawerLayout = findViewById(R.id.drawerLayout_drawer_mainActivity);
        toolbar = findViewById(R.id.toolbar_mainToolbar_mainActivity);
        navigationView = findViewById(R.id.navigationView_navigationContent_mainActivity);
//        viewPagerCalender = findViewById(R.id.viewPager_calender_mainActivity);
        linearLayoutEmptyScreenLayout = findViewById(R.id.linearLayout_emptyScreenDialog_mainActivity);
    }

    private void oneTimeOperationMethod() {
        int check = sharedPreferences.getInt(Constants.sharedPreferenceOneTimeCheckKey, 0);
        if (check == 0) {
            ArrayList<CategoryClass> categoryClassesList = new ArrayList<>();
            String[] expenseArray = getResources().getStringArray(R.array.expense_categories);

            for (String s : expenseArray) {
                CategoryClass categoryClassObject = new CategoryClass();
                categoryClassObject.categoryName = s;
                categoryClassObject.categoryType = Constants.EXPENSE;
                categoryClassObject.categoryIconNumber = 1;
                categoryClassObject.categoryColorNumber = 1;
                categoryClassesList.add(categoryClassObject);
            }

            String[] incomeArray = getResources().getStringArray(R.array.income_categories);

            for (String s : incomeArray) {
                CategoryClass categoryClassObject = new CategoryClass();
                categoryClassObject.categoryName = s;
                categoryClassObject.categoryType = Constants.INCOME;
                categoryClassObject.categoryIconNumber = 1;
                categoryClassObject.categoryColorNumber = 1;
                categoryClassesList.add(categoryClassObject);
            }

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
        if (eventMessage.getEventCode() == 9) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.sharedPreferenceOneTimeCheckKey, 1);
            editor.apply();
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

