package com.example.expensetracker.Adapters;

import static com.example.expensetracker.Utilities.AppUtilityKt.parseAmount;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.Model.TransactionClass;
import com.example.expensetracker.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.ViewHolder> {
    Context mContext;
    List<TransactionClass> transactionList;
    DateFormat dateFormat ;

    public TransactionViewAdapter(Context mContext, List<TransactionClass> list) {
        this.mContext = mContext;
        transactionList = list;
        dateFormat = new SimpleDateFormat("dd/E/yyyy");
    }

    @NonNull
    @Override
    public TransactionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_view_holder,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewAdapter.ViewHolder holder, int position) {
        holder.note.setText(transactionList.get(position).getNote());


            // Expense/Income logic
            if(transactionList.get(position).getType().toString().equals("Expense")) {
                holder.amount.setText("-₹" + parseAmount(transactionList.get(position).getAmount()));
                holder.body.setBackground(mContext.getResources().getDrawable(R.drawable.transaction_single_rectangle_background, mContext.getTheme()));
                holder.amount.setTextColor(mContext.getResources().getColor(R.color.red, mContext.getTheme()));
                holder.body.getBackground().setTint(mContext.getResources().getColor(R.color.red, mContext.getTheme()));
                holder.body.getBackground().setAlpha(25);
            }else{
                holder.amount.setText("+₹" + parseAmount(transactionList.get(position).getAmount()));
                holder.body.setBackground(mContext.getResources().getDrawable(R.drawable.transaction_single_rectangle_background, mContext.getTheme()));
                holder.amount.setTextColor(mContext.getResources().getColor(R.color.green, mContext.getTheme()));
                holder.body.getBackground().setTint(mContext.getResources().getColor(R.color.green, mContext.getTheme()));
                holder.body.getBackground().setAlpha(25);
            }

            //setting day and date logic
            if (!convertDateToString(transactionList.get(position).getDateWithTime()).equals(convertDateToString(getCurrentDate()))){
                //Log.d("time",convertDateToString(transactionList.get(position).getDate())+" "+convertDateToString(getCurrentDate()));
                holder.circleBackground.setBackgroundResource(0);
                holder.circleBackground.setPadding(0,0,0,0);
                holder.date.setTextColor(mContext.getResources().getColor(R.color.black, mContext.getTheme()));
            }

            //eliminate date for multiple entries in same day
            if(position == 0){
                String[] dateSplit = convertDateToString(transactionList.get(position).getDateWithTime()).split("/");
                holder.day.setText(dateSplit[1]);
                holder.date.setText(dateSplit[0]);
            }else{
                String current = convertDateToString(transactionList.get(position).getDateWithTime());
                String prev = convertDateToString(transactionList.get(position-1).getDateWithTime());
                //entries are on similar date to previous
                if(current.equals(prev)){
                    holder.dayDateView.setVisibility(View.INVISIBLE);
                }else{
                    //entries are on diff date so show the date
                    String[] dateSplit = current.split("/");
                    holder.day.setText(dateSplit[1]);
                    holder.date.setText(dateSplit[0]);
                }
            }

    }

    private Long getCurrentDate(){
        return Calendar.getInstance().getTimeInMillis();
    }

    private String convertDateToString(Long dateInMillis){
        Date date = new Date(dateInMillis);
        return dateFormat.format(date);

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note, amount,day,date;
        private RelativeLayout body,circleBackground;
        private LinearLayout dayDateView,main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            note = itemView.findViewById(R.id.textView_note_transactionViewHolder);
            amount = itemView.findViewById(R.id.textView_amount_transactionViewHolder);
            body = itemView.findViewById(R.id.relativeLayout_singleEntry_transactionViewHolder);
            day = itemView.findViewById(R.id.textView_day_transactionViewHolder);
            date = itemView.findViewById(R.id.textView_date_transactionViewHolder);
            dayDateView = itemView.findViewById(R.id.linearLayout_dateDay_transactionViewHolder);
            circleBackground = itemView.findViewById(R.id.relativeLayout_circleBackground_transactionViewHolder);
        }
    }
}
