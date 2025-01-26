package com.roaa.expensetracker.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.roaa.expensetracker.Adapters.TransactionViewAdapter
import com.roaa.expensetracker.Composables.ExpenseTrackerTheme
import com.roaa.expensetracker.Composables.components.AddBottomSheet
import com.roaa.expensetracker.Fragments.DayViewFragment
import com.roaa.expensetracker.Fragments.ListViewFragment
import com.roaa.expensetracker.Fragments.MonthViewFragment
import com.roaa.expensetracker.R
import com.roaa.expensetracker.ViewModels.CategoryViewModel
import com.roaa.expensetracker.ViewModels.TransactionsViewModel
import com.roaa.expensetracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val appBarConfiguration: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding

    private var fabAddExpense: FloatingActionButton? = null

    private var context: Context? = null
    private val transactionViewAdapter: TransactionViewAdapter? = null
    private val transactionViewModel: TransactionsViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var drawerLayout: DrawerLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private var toolbar: MaterialToolbar? = null
    private var navigationView: NavigationView? = null

    private val viewPagerCalender: ViewPager? = null
    private var fragmentTransaction: FragmentTransaction? = null

    private var listViewFragment: ListViewFragment? = null
    private var monthViewFragment: MonthViewFragment? = null
    private var dayViewFragment: DayViewFragment? = null

    private var selectedItem: Int = R.id.item_listView_sideNavigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                TRANSPARENT, TRANSPARENT
            )
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // method to initialize vars
        initVars()

        // method to initialize views
        initViews()

        binding.bottomSheetAddActivityMain.setContent {
            ExpenseTrackerTheme {
                AddBottomSheet()
            }
        }
        fabAddExpense!!.setOnClickListener { v: View? ->
            GlobalScope.launch {
                transactionViewModel.bottomSheetStatus.emit(true)
            }
        }

        toolbar!!.setNavigationOnClickListener { v: View? -> drawerLayout!!.open() }

        navigationView!!.setNavigationItemSelectedListener { v: MenuItem ->
            sideMenuItemClickHandler(
                v
            )
        }

        setUpTransactionFragment()

    }

    private fun setUpTransactionFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(
                R.id.frameLayout_fragment_mainActivity,
                ListViewFragment()
            )
            fragmentTransaction!!.commitAllowingStateLoss()
        }
    }

    private fun setUpMonthFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(
                R.id.frameLayout_fragment_mainActivity,
                MonthViewFragment()
            )
            fragmentTransaction!!.addToBackStack("MonthView")
            fragmentTransaction!!.commit()
        }
    }

    private fun setUpDayFragment() {
        Handler().post {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.replace(
                R.id.frameLayout_fragment_mainActivity,
                DayViewFragment()
            )
            fragmentTransaction!!.addToBackStack("DayView")
            fragmentTransaction!!.commit()
        }
    }


    private fun sideMenuItemClickHandler(v: MenuItem): Boolean {
        Handler().postDelayed({
            val menuID = v.itemId
            if (menuID == R.id.item_listView_sideNavigation) {
                setUpTransactionFragment()
                selectedItem = R.id.item_listView_sideNavigation
            } else if (menuID == R.id.item_monthView_sideNavigation) {
                setUpMonthFragment()
                selectedItem = R.id.item_monthView_sideNavigation
            } else if (menuID == R.id.item_dayView_sideNavigation) {
                setUpDayFragment()
                selectedItem = R.id.item_dayView_sideNavigation
            } else if (menuID == R.id.item_category_sideNavigation) {
                sendUserToCategoryActivity()
                selectedItem = R.id.item_category_sideNavigation
            } else if (menuID == R.id.item_expenseCategory_sideNavigation) {
                sendUserToExpenseCategoryActivity()
                selectedItem = R.id.item_listView_sideNavigation
            } else if (menuID == R.id.item_incomeCategory_sideNavigation) {
                sendUserToIncomeCategoryActivity()
                selectedItem = R.id.item_listView_sideNavigation
            } else if (menuID == R.id.item_settings_sideNavigation) {
                sendUserToSettingsActivity()
                selectedItem = R.id.item_listView_sideNavigation
            } else if (menuID == R.id.item_analyze_sideNavigation) {
                selectedItem = R.id.item_listView_sideNavigation
                sendUserToStatisticsActivity()
            } else if (menuID == R.id.item_bankAccounts_sideNavigation) {
                sendUserToPaymentMethodsActivity()
            } else if (menuID == R.id.item_test1_sideNavigation) {
                sendUserToAddActivity()
            } else if (menuID == R.id.item_test2_sideNavigation) {
                sendUserToManageAccountsActivity()
            }
        }, 300)
        navigationView?.setCheckedItem(selectedItem)
        drawerLayout!!.close()
        return true
    }

    private fun initVars() {
        context = applicationContext
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


    private fun sendUserToAddActivity() {
        val intent = Intent(context, AddActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToExpenseCategoryActivity() {
        val intent = Intent(context, ExpenseCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToIncomeCategoryActivity() {
        val intent = Intent(context, IncomeCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToSettingsActivity() {
        val intent = Intent(context, SettingComposeActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToStatisticsActivity() {
        val intent = Intent(context, StatisticsActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToCategoryActivity() {
        val intent = Intent(context, CategoryActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToManageAccountsActivity() {
        val intent = Intent(context, ManageBankAccountActivity::class.java)
        startActivity(intent)
    }

    private fun sendUserToPaymentMethodsActivity() {
        val intent = Intent(context, PaymentMethodsComposeActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItem", selectedItem)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val i = savedInstanceState.getInt("selectedItem")
        if (i == R.id.item_listView_sideNavigation) {
            setUpTransactionFragment()
            selectedItem = R.id.item_listView_sideNavigation
        } else if (i == R.id.item_monthView_sideNavigation) {
            setUpMonthFragment()
            selectedItem = R.id.item_monthView_sideNavigation
        } else if (i == R.id.item_dayView_sideNavigation) {
            setUpDayFragment()
            selectedItem = R.id.item_dayView_sideNavigation
        }
    }
}

