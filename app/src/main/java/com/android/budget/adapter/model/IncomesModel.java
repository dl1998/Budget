package com.android.budget.adapter.model;

import com.android.budget.entity.Income;

import java.sql.Date;

/**
 * Created by dl1998 on 05.11.17.
 */

public class IncomesModel {

    private Integer id_income;
    private Float cost_income;
    private Date date_income;
    private String currency;
    private Integer id_account;

    private IncomesModel() {
    }

    public IncomesModel(Income income, String currency, Integer id_account) {
        this.id_income = income.getId_income();
        this.cost_income = income.getCost_income();
        this.date_income = income.getDate_income();
        this.currency = currency;
        this.id_account = id_account;
    }

    public Integer getId_income() {
        return id_income;
    }

    public void setId_income(Integer id_income) {
        this.id_income = id_income;
    }

    public Float getCost_income() {
        return cost_income;
    }

    public void setCost_income(Float cost_income) {
        this.cost_income = cost_income;
    }

    public Date getDate_income() {
        return date_income;
    }

    public void setDate_income(Date date_income) {
        this.date_income = date_income;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId_account() {
        return id_account;
    }

    public void setId_account(Integer id_account) {
        this.id_account = id_account;
    }
}
