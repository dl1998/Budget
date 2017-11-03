package com.android.budget.entity;

import java.sql.Date;

/**
 * Created by dl1998 on 29.10.17.
 */

public class Income {

    private Integer id_income;
    private Date date_income;
    private Float cost_income;
    private Integer id_account;

    public Income() {
    }

    public Income(Integer id_income, Date date_income, Float cost_income, Integer id_account) {
        this.id_income = id_income;
        this.date_income = date_income;
        this.cost_income = cost_income;
        this.id_account = id_account;
    }

    public Integer getId_income() {
        return id_income;
    }

    public void setId_income(Integer id_income) {
        this.id_income = id_income;
    }

    public Date getDate_income() {
        return date_income;
    }

    public void setDate_income(Date date_income) {
        this.date_income = date_income;
    }

    public Float getCost_income() {
        return cost_income;
    }

    public void setCost_income(Float cost_income) {
        this.cost_income = cost_income;
    }

    public Integer getId_account() {
        return id_account;
    }

    public void setId_account(Integer id_account) {
        this.id_account = id_account;
    }
}
