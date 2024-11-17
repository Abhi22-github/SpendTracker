package com.example.expensetracker.Model;

import java.time.LocalDate;

public class Day {
    private LocalDate date;

    public Day(){

    }

    public Day(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
