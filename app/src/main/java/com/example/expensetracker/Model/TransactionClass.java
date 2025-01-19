package com.example.expensetracker.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "transaction_table")
public class TransactionClass {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private Long amount;
    private String note;
    private String category;
    private Integer categoryIcon;
    private Long dateWithTime;
    private Long date;

    public TransactionClass() {

    }

    public TransactionClass(String type, Long amount, String note, String category, Integer categoryIcon, Long dateWithTime, Long date) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.category = category;
        this.categoryIcon = categoryIcon;
        this.dateWithTime = dateWithTime;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getDateWithTime() {
        return dateWithTime;
    }

    public void setDateWithTime(Long dateWithTime) {
        this.dateWithTime = dateWithTime;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(Integer categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
}
