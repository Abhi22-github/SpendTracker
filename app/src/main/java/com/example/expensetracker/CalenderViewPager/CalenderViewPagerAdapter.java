package com.example.expensetracker.CalenderViewPager;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expensetracker.Model.Day;
import com.example.expensetracker.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalenderViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ViewGroup viewContainer = null;
    private Integer MAX_VALUE;
    private LocalDate todayDate;

    private LocalDate selectedDate;
    private ArrayList<Day> daysList;
    DateTimeFormatter dateTimeFormatter;

    public CalenderViewPagerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        MAX_VALUE = 500;
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        todayDate = LocalDate.now();
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
        daysList = daysInMonthArray(position);

        //setting up the recycler view with current month dates
        recyclerView.setAdapter(new DaysOfMonthAdapter(context, daysList) {
            @Override
            void onBindViewHolder(RecyclerView.ViewHolder holder, Day day) {
                CalenderViewPagerAdapter.this.onBindView(holder.itemView, day);
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(CalenderViewPagerAdapter.this.onCreateView(parent, viewType)) {
                };
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


    public void onBindView(View view, Day day) {
        TextView textViewDate = view.findViewById(R.id.textView_date_singleDateViewHolder);
        RelativeLayout relativeLayoutDateCircle = view.findViewById(R.id.relativeLayout_circleBackground_singleDateViewHolder);
        String[] dateSplit = String.valueOf(day.getDate()).split("-");
        String str = day.getDate().format(dateTimeFormatter);
        //textView.setText(dateSplit[2]+"-"+dateSplit[1]);
        if(todayDate.toString().equals(day.getDate().toString())){
            relativeLayoutDateCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_background, context.getTheme()));
            relativeLayoutDateCircle.getBackground().setTint(context.getResources().getColor(R.color.black, context.getTheme()));
            textViewDate.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
        }
        textViewDate.setText(dateSplit[2]);
    }

    //calculate the days in month
    public ArrayList<Day> daysInMonthArray(Integer position) {

        position = position - 250;

        ArrayList<Day> daysInMonthArray = new ArrayList<>();
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
                day.setDate(prevMonthSameDate.withDayOfMonth((Math.abs(daysInPrevMonth-Math.abs(dayOfWeek-(i+1))))));
                daysInMonthArray.add(day);
            } else if (i >= daysInMonth + dayOfWeek) {
                //next month condition
                day.setDate(nextMonthSameDate.withDayOfMonth(Math.abs((i + 1) - (daysInMonth + dayOfWeek))));
                daysInMonthArray.add(day);
            } else if (i < daysInMonth + dayOfWeek) {
                //current month condition
                day.setDate(selectedDate.withDayOfMonth((i + 1) - dayOfWeek));
                daysInMonthArray.add(day);
            }
           // Log.d("position<>", String.valueOf(day.getDate())+ " " + String.valueOf(i));

        }
        return daysInMonthArray;
    }


}




