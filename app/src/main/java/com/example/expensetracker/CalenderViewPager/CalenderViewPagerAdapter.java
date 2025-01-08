package com.example.expensetracker.CalenderViewPager;


import static com.example.expensetracker.Utilities.AppUtilityKt.LocalDateToLong;
import static com.example.expensetracker.Utilities.AppUtilityKt.convertLocalDateToLong;
import static com.example.expensetracker.Utilities.AppUtilityKt.convertTotalExpenseIncomeClassToMap;
import static com.example.expensetracker.Utilities.AppUtilityKt.parseAmount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expensetracker.Activity.MainActivity;
import com.example.expensetracker.Fragments.DayViewFragment;
import com.example.expensetracker.Model.Day;
import com.example.expensetracker.Model.TotalExpenseIncomeClass;
import com.example.expensetracker.R;
import com.example.expensetracker.ViewModels.AddActivityViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kotlin.Pair;

public class CalenderViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ViewGroup viewContainer = null;
    private Integer MAX_VALUE;
    private LocalDate todayDate;

    private LocalDate selectedDate;
    private ArrayList<LocalDate> daysList;
    private HashMap<Long, Pair<Long, Long>> map;

    DateTimeFormatter dateTimeFormatter;
    private AddActivityViewModel viewModel;

    public CalenderViewPagerAdapter(Context context, AddActivityViewModel viewModel) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        MAX_VALUE = 500;
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        todayDate = LocalDate.now();
        this.viewModel = viewModel;
        map = new HashMap<>();
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

        daysList = daysInMonthArray(position);
        viewModel.getListOfTotalAmountPerDayForRange(LocalDateToLong(daysList.get(0)), LocalDateToLong(daysList.get(daysList.size()-1))).observe((LifecycleOwner) context, new Observer<List<TotalExpenseIncomeClass>>() {
            @Override
            public void onChanged(List<TotalExpenseIncomeClass> totalExpenseIncomeClasses) {
                map = convertTotalExpenseIncomeClassToMap(totalExpenseIncomeClasses);
                //getting the current page month days in array
                daysList = daysInMonthArray(position);
                DaysOfMonthAdapter daysOfMonthAdapter = new DaysOfMonthAdapter(context, daysList, map) {
                    @Override
                    void onBindViewHolder(RecyclerView.ViewHolder holder, LocalDate date, HashMap<Long, Pair<Long, Long>> map) {
                        CalenderViewPagerAdapter.this.onBindView(holder.itemView, date, map);
                    }

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(CalenderViewPagerAdapter.this.onCreateView(parent, viewType)) {
                        };
                    }
                };
                recyclerView.setAdapter(daysOfMonthAdapter);
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


    public void onBindView(View view, LocalDate date, HashMap<Long, Pair<Long, Long>> map) {
        TextView textViewDate = view.findViewById(R.id.textView_date_singleDateViewHolder);
        TextView textViewExpenseAmount = view.findViewById(R.id.textView_expenseAmount_singleDateViewHolder);
        TextView textViewIncomeAmount = view.findViewById(R.id.textView_incomeAmount_singleDateViewHolder);
        RelativeLayout relativeLayoutDateCircle = view.findViewById(R.id.relativeLayout_circleBackground_singleDateViewHolder);
        RelativeLayout relativeLayoutExpenseBox = view.findViewById(R.id.relativeLayout_backgroundExpense_singleDateViewHolder);
        RelativeLayout relativeLayoutIncomeBox = view.findViewById(R.id.relativeLayout_backgroundIncome_singleDateViewHolder);
        LinearLayout itemLayout = view.findViewById(R.id.linearLayout_singleItem_singleDateViewHolder);
        String[] dateSplit = String.valueOf(date).split("-");

        if (todayDate.toString().equals(String.valueOf(date))) {
            relativeLayoutDateCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_background, context.getTheme()));
            relativeLayoutDateCircle.getBackground().setTint(context.getResources().getColor(R.color.black, context.getTheme()));
            textViewDate.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
        }
        textViewDate.setText(dateSplit[2]);
        if (map.containsKey(convertLocalDateToLong(date))) {
            if (Objects.requireNonNull(map.get(convertLocalDateToLong(date))).getFirst() != 0) {
                textViewExpenseAmount.setText("-₹" + parseAmount(map.get(convertLocalDateToLong(date)).getFirst()));
                relativeLayoutExpenseBox.setVisibility(View.VISIBLE);
            }
            if (Objects.requireNonNull(map.get(convertLocalDateToLong(date))).getSecond() != 0) {
                textViewIncomeAmount.setText("+₹" + parseAmount(map.get(convertLocalDateToLong(date)).getSecond()));
                relativeLayoutIncomeBox.setVisibility(View.VISIBLE);
            }
        }

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    viewModel.setCurrentSelectedDate(date);
                    FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout_fragment_mainActivity,new DayViewFragment());
                    fragmentTransaction.addToBackStack("DayDetails");
                    fragmentTransaction.commit();
                } catch (ClassCastException e) {
                    Toast.makeText(context,"Can't get fragment Manager"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //calculate the days in month
    public ArrayList<LocalDate> daysInMonthArray(Integer position) {

        position = position - 250;

        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        if (position > 0) {
            selectedDate = LocalDate.now().plusMonths(position).withDayOfMonth(15);
        } else if (position < 0) {
            selectedDate = LocalDate.now().minusMonths(Math.abs(position)).withDayOfMonth(15);
        } else {
            selectedDate = LocalDate.now();
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
                daysInMonthArray.add(day.getDate());
            } else if (i >= daysInMonth + dayOfWeek) {
                //next month condition
                day.setDate(nextMonthSameDate.withDayOfMonth(Math.abs((i + 1) - (daysInMonth + dayOfWeek))));
                daysInMonthArray.add(day.getDate());
            } else if (i < daysInMonth + dayOfWeek) {
                //current month condition
                day.setDate(selectedDate.withDayOfMonth((i + 1) - dayOfWeek));
                daysInMonthArray.add(day.getDate());
            }

        }
        return daysInMonthArray;
    }

}




