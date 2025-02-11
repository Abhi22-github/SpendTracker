package com.roaa.expensetracker.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "transaction_table")
public class TransactionClass {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private Float amount;
    private String note;
    private Long dateWithTime;
    private Long date;;
    private long categoryId;  // foreign key for category
    private long bankAccountId; // foreign key with bank

    public TransactionClass() {

    }

    public TransactionClass(String type, Float amount, String note, Long dateWithTime, Long date, long categoryId, long bankAccountId) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.dateWithTime = dateWithTime;
        this.date = date;
        this.categoryId = categoryId;
        this.bankAccountId = bankAccountId;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
