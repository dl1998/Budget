package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.Converter;
import com.android.budget.dao.ExpensesDAO;
import com.android.budget.entity.Expenses;

import java.sql.Date;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public class ExpensesDAOImpl extends StandardDAOImpl<Expenses> implements ExpensesDAO {

    private String TABLE_NAME = "expenses";

    public ExpensesDAOImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    Expenses getByCursor(Cursor cursor) {
        Expenses expenses = new Expenses();

        expenses.setId_expenses(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        expenses.setDate_expenses(Converter.getDate(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)))));
        expenses.setCost_expenses(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(2))));
        expenses.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return expenses;
    }

    @Override
    SQLiteStatement bind(SQLiteStatement statement, Expenses expenses, boolean update) {

        statement.clearBindings();
        statement.bindString(1, Converter.getTextDate(expenses.getDate_expenses()));
        statement.bindDouble(2, expenses.getCost_expenses());
        statement.bindLong(3, expenses.getId_category());
        if (update) statement.bindLong(4, expenses.getId_expenses());

        return statement;
    }

    @Override
    public Expenses findExpensesById(Integer id) {

        Log.d("myDB", "Expenses findExpensesById start");

        List<Expenses> list = super.get(TABLE_NAME, "id_expenses = ?", new String[]{String.valueOf(id)}, null);

        Log.d("myDB", "Expenses findExpensesById end");

        return list.get(0);
    }

    @Override
    public List<Expenses> getAll() {
        Log.d("myDB", "Expenses getAll start");

        List<Expenses> list = super.get(TABLE_NAME, null, null, null);

        Log.d("myDB", "Expenses getAll end");

        return list;
    }

    @Override
    public List<Expenses> getAllByCategory(Integer categoryId) {

        Log.d("myDB", "Expenses getAllByCategory start");

        List<Expenses> list = super.get(TABLE_NAME, "id_category = ?", new String[]{String.valueOf(categoryId)}, null);

        Log.d("myDB", "Expenses getAllByCategory end");

        return list;
    }

    @Override
    public List<Expenses> getAllByDateForCategory(Date date, Integer categoryId) {

        Log.d("myDB", "Expenses getAllByDateForCategory start");

        List<Expenses> list = super.get(TABLE_NAME, "id_category = ? and date_expenses = ?",
            new String[]{String.valueOf(categoryId), Converter.getTextDate(date)}, null);

        Log.d("myDB", "Expenses getAllByDateForCategory end");

        return list;
    }

    @Override
    public void add(Expenses expenses) {

        Log.d("myDB", "Expenses add start");

        String SQL = String.format("INSERT INTO %s (date_expenses, cost_expenses, id_category) VALUES(?, ?, ?);", TABLE_NAME);
        super.add(expenses, SQL);

        Log.d("myDB", "Expenses add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Expenses removeAll start");

        super.remove(String.format("DELETE FROM %s;", TABLE_NAME));

        Log.d("myDB", "Expenses removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Expenses removeById start");

        super.remove(String.format("DELETE FROM %s WHERE id_expenses = %d;", TABLE_NAME, id));

        Log.d("myDB", "Expenses removeById end");

    }

    @Override
    public void updateById(Expenses expenses) {

        Log.d("myDB", "Expenses updateById start");

        String SQL = String.format("UPDATE %s SET date_expenses = ?, cost_expenses = ?, id_category = ? " +
            "WHERE id_expenses = ?;", TABLE_NAME);
        super.update(expenses, SQL);

        Log.d("myDB", "Expenses updateById end");

    }
}