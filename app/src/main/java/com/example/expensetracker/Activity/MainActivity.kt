package com.example.expensetracker.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager.widget.ViewPager
import com.example.expensetracker.Adapters.TransactionViewAdapter
import com.example.expensetracker.Events.EventMessage
import com.example.expensetracker.Fragments.DayViewFragment
import com.example.expensetracker.Fragments.ListViewFragment
import com.example.expensetracker.Fragments.MonthViewFragment
import com.example.expensetracker.Model.CategoryClass
import com.example.expensetracker.R
import com.example.expensetracker.Utilities.Constants
import com.example.expensetracker.ViewModels.AddActivityViewModel
import com.example.expensetracker.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    private val appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null

    private var fabAddExpense: FloatingActionButton? = null

    private var context: Context? = null
    private val transactionViewAdapter: TransactionViewAdapter? = null
    private var viewModel: AddActivityViewModel? = null
    private var drawerLayout: DrawerLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var toolbar: MaterialToolbar? = null
    private var navigationView: NavigationView? = null

    private val viewPagerCalender: ViewPager? = null
    private var sharedPreferences: SharedPreferences? = null
    private var fragmentTransaction: FragmentTransaction? = null

    private var listViewFragment: ListViewFragment? = null
    private var monthViewFragment: MonthViewFragment? = null
    private var dayViewFragment: DayViewFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel = ViewModelProvider(this).get(
            AddActivityViewModel::class.java
        )
        viewModel!!.initializeDatabaseRepository(application)

        // method to initialize vars
        initVars()

        // method to initialize views
        initViews()

        fabAddExpense!!.setOnClickListener { v: View? -> sendUserToAddActivity() }

        toolbar!!.setNavigationOnClickListener { v: View? -> drawerLayout!!.open() }

        navigationView!!.setNavigationItemSelectedListener { v: MenuItem ->
            sideMenuItemClickHandler(
                v
            )
        }

        setUpTransactionFragment()

        oneTimeOperationMethod()
    }

    private fun setUpTransactionFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(
                R.id.frameLayout_fragment_mainActivity,
                listViewFragment!!
            )
            fragmentTransaction!!.commit()
        }
    }

    private fun setUpMonthFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(
                R.id.frameLayout_fragment_mainActivity,
                monthViewFragment!!
            )
            fragmentTransaction!!.addToBackStack("MonthView")
            fragmentTransaction!!.commit()
        }
    }

    private fun setUpDayFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(R.id.frameLayout_fragment_mainActivity, dayViewFragment!!)
            fragmentTransaction!!.addToBackStack("DayView")
            fragmentTransaction!!.commit()
        }
    }

    private fun sideMenuItemClickHandler(v: MenuItem): Boolean {
        Handler().postDelayed({
            val menuID = v.itemId
            if (menuID == R.id.item_listView_sideNavigation) {
                setUpTransactionFragment()
                navigationView!!.setCheckedItem(R.id.item_listView_sideNavigation)
            } else if (menuID == R.id.item_monthView_sideNavigation) {
                setUpMonthFragment()
                navigationView!!.setCheckedItem(R.id.item_monthView_sideNavigation)
            } else if (menuID == R.id.item_dayView_sideNavigation) {
                setUpDayFragment()
                navigationView!!.setCheckedItem(R.id.item_dayView_sideNavigation)
            } else if (menuID == R.id.item_expenseCategory_sideNavigation) {
                sendUserToExpenseCategoryActivity()
                navigationView!!.setCheckedItem(R.id.item_listView_sideNavigation)
            } else if (menuID == R.id.item_incomeCategory_sideNavigation) {
                sendUserToIncomeCategoryActivity()
                navigationView!!.setCheckedItem(R.id.item_listView_sideNavigation)
            } else if (menuID == R.id.item_settings_sideNavigation) {
                sendUserToSettingsActivity()
                navigationView!!.setCheckedItem(R.id.item_listView_sideNavigation)
            } else if (menuID == R.id.item_analyze_sideNavigation) {
                navigationView!!.setCheckedItem(R.id.item_listView_sideNavigation)
                sendUserToStatisticsActivity()
            } else if (menuID == R.id.item_bankAccounts_sideNavigation) {
                Toast.makeText(context, "manageBankAccounts", Toast.LENGTH_SHORT).show()
            } else {
            }
        }, 300)
        drawerLayout!!.close()
        return true
    }

    private fun initVars() {
        context = applicationContext
        sharedPreferences = getSharedPreferences(Constants.sharedPreferencesName, MODE_PRIVATE)
        listViewFragment = ListViewFragment()
        monthViewFragment = MonthViewFragment()
        dayViewFragment = DayViewFragment()
    }


    private fun initViews() {
        fabAddExpense = findViewById(R.id.fab_add_activity_main)
        appBarLayout = findViewById(R.id.appBarLayout_appBar_mainActivity)
        drawerLayout = findViewById(R.id.drawerLayout_drawer_mainActivity)
        toolbar = findViewById(R.id.toolbar_mainToolbar_mainActivity)
        navigationView = findViewById(R.id.navigationView_navigationContent_mainActivity)
    }

    private fun oneTimeOperationMethod() {
        val check = sharedPreferences!!.getInt(Constants.sharedPreferenceOneTimeCheckKey, 0)
        if (check == 0) {
            val categoryClassesList = ArrayList<CategoryClass>()
            val expenseArray = resources.getStringArray(R.array.expense_categories)

            for (s in expenseArray) {
                val categoryClassObject = CategoryClass()
                categoryClassObject.categoryName = s
                categoryClassObject.categoryType = Constants.EXPENSE
                categoryClassObject.categoryIconNumber = 1
                categoryClassObject.categoryColorNumber = 1
                categoryClassesList.add(categoryClassObject)
            }

            val incomeArray = resources.getStringArray(R.array.income_categories)

            for (s in incomeArray) {
                val categoryClassObject = CategoryClass()
                categoryClassObject.categoryName = s
                categoryClassObject.categoryType = Constants.INCOME
                categoryClassObject.categoryIconNumber = 1
                categoryClassObject.categoryColorNumber = 1
                categoryClassesList.add(categoryClassObject)
            }

            viewModel!!.fillCategoriesInDatabase(categoryClassesList)

            val editor = sharedPreferences!!.edit()
            editor.putInt(Constants.sharedPreferenceOneTimeCheckKey, 1)
            editor.apply()
        }
    }

    private fun sendUserToAddActivity() {
        val intent = Intent(context, AddActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToExpenseCategoryActivity() {
        val intent = Intent(context, ExpenseCategoryActivity::class.java)
        startActivity(intent)
        //MainActivity.this.overridePendingTransition(android.R.anim.fade_in, 0);
    }

    private fun sendUserToIncomeCategoryActivity() {
        val intent = Intent(context, IncomeCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToSettingsActivity() {
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToStatisticsActivity() {
        val intent = Intent(context, StatisticsActivity::class.java)
        startActivity(intent)
    }

    @Subscribe
    fun EventHandler(eventMessage: EventMessage) {
        if (eventMessage.getEventCode() == 9) {
            val editor = sharedPreferences!!.edit()
            editor.putInt(Constants.sharedPreferenceOneTimeCheckKey, 1)
            editor.apply()
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
}

