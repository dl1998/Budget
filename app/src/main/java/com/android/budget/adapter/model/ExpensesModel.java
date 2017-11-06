package com.android.budget.adapter.model;

import android.database.sqlite.SQLiteDatabase;

import com.android.budget.Converter;
import com.android.budget.dao.impl.ExpensesDAOImpl;
import com.android.budget.entity.Category;
import com.android.budget.entity.Expenses;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by dl1998 on 01.11.17.
 */

public class ExpensesModel {

    private String currency_ISO;
    private Category category;
    private ArrayList<Expenses> expensesList;

    private ExpensesModel() {
    }

    public ExpensesModel(SQLiteDatabase db, String currency_ISO, Category category, Date date) {
        this.currency_ISO = currency_ISO;
        this.category = category;
        this.expensesList = new ArrayList<>();

        ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
        this.expensesList.addAll(expensesDAO.getAllByDateForCategory(Converter.getDate(date), category.getId_category()));
    }

    public String getCurrency_ISO() {
        return currency_ISO;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<Expenses> getExpensesList() {
        return expensesList;
    }

}
