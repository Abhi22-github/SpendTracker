package com.example.expensetracker.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table",indices = {@Index(value = {"categoryName","categoryType"},unique = true)})
public class CategoryClass {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "categoryName")
    private String categoryName;
    private Integer categoryColorNumber;
    private Integer categoryIconNumber;
    @ColumnInfo(name = "categoryType")
    private String categoryType;

    public CategoryClass() {
    }

    public CategoryClass(String categoryName, Integer categoryColorNumber, Integer categoryIconNumber
            , String categoryType) {
        this.categoryName = categoryName;
        this.categoryColorNumber = categoryColorNumber;
        this.categoryIconNumber = categoryIconNumber;
        this.categoryType = categoryType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryColorNumber() {
        return categoryColorNumber;
    }

    public void setCategoryColorNumber(Integer categoryColorNumber) {
        this.categoryColorNumber = categoryColorNumber;
    }

    public Integer getCategoryIconNumber() {
        return categoryIconNumber;
    }

    public void setCategoryIconNumber(Integer categoryIconNumber) {
        this.categoryIconNumber = categoryIconNumber;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
