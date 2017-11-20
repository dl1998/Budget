package com.android.budget.entity;

import java.sql.Date;

/**
 * Created by dl1998 on 29.10.17.
 */

public class Expenses {

    private Integer id_expenses;
    private Date date_expenses;
    private Float cost_expenses;
    private Integer id_category;

    public Expenses() {
    }

    public Expenses(Integer id_expenses, Date date_expenses, Float cost_expenses, Integer id_category) {
        this.id_expenses = id_expenses;
        this.date_expenses = date_expenses;
        this.cost_expenses = cost_expenses;
        this.id_category = id_category;
    }

    public Integer getId_expenses() {
        return id_expenses;
    }

    public void setId_expenses(Integer id_expenses) {
        this.id_expenses = id_expenses;
    }

    public Date getDate_expenses() {
        return date_expenses;
    }

    public void setDate_expenses(Date date_expenses) {
        this.date_expenses = date_expenses;
    }

    public Float getCost_expenses() {
        return cost_expenses;
    }

    public void setCost_expenses(Float cost_expenses) {
        this.cost_expenses = cost_expenses;
    }

    public Integer getId_category() {
        return id_category;
    }

    public void setId_category(Integer id_category) {
        this.id_category = id_category;
    }
}
