package com.android.budget.adapter.model;

import com.android.budget.entity.Category;

import java.sql.Date;

/**
 * Created by dl1998 on 01.11.17.
 */

public class IncomeExpensesModel {

    private Float cost;
    private Date date;
    private Category category;

    public IncomeExpensesModel() {
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
