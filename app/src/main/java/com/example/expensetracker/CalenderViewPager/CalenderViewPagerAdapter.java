package com.example.expensetracker.CalenderViewPager;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expensetracker.Activity.MainActivity;
import com.example.expensetracker.Adapters.TransactionViewAdapter;
import com.example.expensetracker.Model.DateWithAmountClass;
import com.example.expensetracker.Model.Day;
import com.example.expensetracker.Model.TotalAmountClass;
import com.example.expensetracker.Model.TransactionClass;
import com.example.expensetracker.R;
import com.example.expensetracker.Utilities.Constants;
import com.example.expensetracker.ViewModels.AddActivityViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalenderViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ViewGroup viewContainer = null;
    private Integer MAX_VALUE;
    private LocalDate todayDate;

    private LocalDate selectedDate;
    private ArrayList<DateWithAmountClass> daysListWithAmount;
    DateTimeFormatter dateTimeFormatter;
    private AddActivityViewModel viewModel;

    public CalenderViewPagerAdapter(Context context, AddActivityViewModel viewModel) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        MAX_VALUE = 500;
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        todayDate = LocalDate.now();
        this.viewModel = viewModel;
    }

    @Override
    public int getCount() {
        return MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //initializing recycler view
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));

        //getting the current page month days in array
        daysListWithAmount = daysInMonthArray(position);

        DaysOfMonthAdapter daysOfMonthAdapter = new DaysOfMonthAdapter(context, daysListWithAmount) {
            @Override
            void onBindViewHolder(RecyclerView.ViewHolder holder, DateWithAmountClass dateWithAmountClass) {
                CalenderViewPagerAdapter.this.onBindView(holder.itemView, dateWithAmountClass);
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(CalenderViewPagerAdapter.this.onCreateView(parent, viewType)) {
                };
            }
        };

        recyclerView.setAdapter(daysOfMonthAdapter);

        //setting up the recycler view with current month dates
        viewModel.getAllTransactions().observe((LifecycleOwner) context, new Observer<List<TransactionClass>>() {
            @Override
            public void onChanged(List<TransactionClass> transactionClasses) {
                daysListWithAmount = daysInMonthArray(position);
                daysOfMonthAdapter.updateData(daysListWithAmount);
                daysOfMonthAdapter.notifyDataSetChanged();
            }
        });

        container.addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewContainer = container;

        return recyclerView;


    }

    //setting up the view and it's height
    private View onCreateView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_date_calender_view, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * .1666666);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void onBindView(View view, DateWithAmountClass dateWithAmountClass) {
        TextView textViewDate = view.findViewById(R.id.textView_date_singleDateViewHolder);
        TextView textViewExpenseAmount = view.findViewById(R.id.textView_expenseAmount_singleDateViewHolder);
        TextView textViewIncomeAmount = view.findViewById(R.id.textView_incomeAmount_singleDateViewHolder);
        RelativeLayout relativeLayoutDateCircle = view.findViewById(R.id.relativeLayout_circleBackground_singleDateViewHolder);
        RelativeLayout relativeLayoutExpenseBox = view.findViewById(R.id.relativeLayout_backgroundExpense_singleDateViewHolder);
        RelativeLayout relativeLayoutIncomeBox = view.findViewById(R.id.relativeLayout_backgroundIncome_singleDateViewHolder);
        LinearLayout itemLayout = view.findViewById(R.id.linearLayout_singleItem_singleDateViewHolder);
        String[] dateSplit = String.valueOf(dateWithAmountClass.getDate()).split("-");
        // String str = dateWithAmountClass.getDate().format(dateTimeFormatter);
        //textView.setText(dateSplit[2]+"-"+dateSplit[1]);
        if (todayDate.toString().equals(String.valueOf(dateWithAmountClass.getDate()))) {
            relativeLayoutDateCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_background, context.getTheme()));
            relativeLayoutDateCircle.getBackground().setTint(context.getResources().getColor(R.color.black, context.getTheme()));
            textViewDate.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));

            itemLayout.setBackground(context.getResources().getDrawable(R.drawable.single_item_rectange_calender_view, context.getTheme()));

        }
        textViewDate.setText(dateSplit[2]);
        if(dateWithAmountClass.getTotalExpenseAmount() != 0){
            textViewExpenseAmount.setText("-"+String.valueOf(dateWithAmountClass.getTotalExpenseAmount()));
        }else{
            relativeLayoutExpenseBox.setVisibility(View.GONE);
        }
        if(dateWithAmountClass.getTotalIncomeAmount() != 0){
            textViewIncomeAmount.setText("-"+String.valueOf(dateWithAmountClass.getTotalIncomeAmount()));
        }else{
            relativeLayoutIncomeBox.setVisibility(View.GONE);
        }

    }

    //calculate the days in month
    public ArrayList<DateWithAmountClass> daysInMonthArray(Integer position) {

        position = position - 250;

        ArrayList<DateWithAmountClass> daysInMonthArray = new ArrayList<>();
        if (position > 0) {

            selectedDate = LocalDate.now().plusMonths(position).withDayOfMonth(15);
            // Log.d("position>0",String.valueOf(position)+" "+selectedDate.toString());
        } else if (position < 0) {

            selectedDate = LocalDate.now().minusMonths(Math.abs(position)).withDayOfMonth(15);
            // Log.d("position<0",String.valueOf(position)+" "+selectedDate.toString());
        } else {

            selectedDate = LocalDate.now();
            // Log.d("position=0",String.valueOf(position)+" "+selectedDate.toString());
        }


        //finding the exact  same date in prev month and next month
        LocalDate prevMonthSameDate = selectedDate.minusMonths(1);
        LocalDate nextMonthSameDate = selectedDate.plusMonths(1);


        //getting the prev and next Month
        YearMonth currentMonth = YearMonth.from(selectedDate);
        YearMonth prevMonth = YearMonth.from(selectedDate).minusMonths(1);
        YearMonth nextMonth = YearMonth.from(selectedDate).plusMonths(1);

        //finding out the days in prev,present and next month
        int daysInPrevMonth = prevMonth.lengthOfMonth();
        int daysInMonth = currentMonth.lengthOfMonth();
        int daysInNextMonth = nextMonth.lengthOfMonth();


        //finding the firstDate of current month
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);

        //getting the day of week for the 1st date of current month
        /*
        0 -> Sunday
        1 -> Monday
        2 -> Tuesday
        3 -> Wednesday
        4 -> Thursday
        5 -> Friday
        6 -> Saturday
         */
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //loop to get 42 entries including some of prev months and some of next months with present month
        for (int i = 0; i < 42; i++) {
            Day day = new Day();
            if (i < dayOfWeek) {
                //prev months condition
                //day.setDate(prevMonthSameDate.withDayOfMonth((Math.abs((i) - daysInPrevMonth))));
                day.setDate(prevMonthSameDate.withDayOfMonth((Math.abs(daysInPrevMonth - Math.abs(dayOfWeek - (i + 1))))));
                daysInMonthArray.add(getTotalForDay(day));
            } else if (i >= daysInMonth + dayOfWeek) {
                //next month condition
                day.setDate(nextMonthSameDate.withDayOfMonth(Math.abs((i + 1) - (daysInMonth + dayOfWeek))));
                daysInMonthArray.add(getTotalForDay(day));
            } else if (i < daysInMonth + dayOfWeek) {
                //current month condition
                day.setDate(selectedDate.withDayOfMonth((i + 1) - dayOfWeek));
                daysInMonthArray.add(getTotalForDay(day));
            }
            // Log.d("position<>", String.valueOf(day.getDate())+ " " + String.valueOf(i));
            //Log.d("Hello",String.valueOf(getTotalForDay(day).get(0).getTotalAmount()));

        }
        return daysInMonthArray;
    }

    private DateWithAmountClass getTotalForDay(Day day) {
        return new DateWithAmountClass(day.getDate(),
                viewModel.getTotalAmountByDateAndCategoryType(Long.parseLong(day.getDate().toString().replace("-", "")), Constants.EXPENSE).getTotalAmount(),
                viewModel.getTotalAmountByDateAndCategoryType(Long.parseLong(day.getDate().toString().replace("-", "")), Constants.INCOME).getTotalAmount()
        );

    }


}




