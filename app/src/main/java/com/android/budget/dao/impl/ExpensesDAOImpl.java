package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.ExpensesDAO;
import com.android.budget.entity.Expenses;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public class ExpensesDAOImpl implements ExpensesDAO {

    private SQLiteDatabase db;

    private ExpensesDAOImpl() {
    }

    public ExpensesDAOImpl(SQLiteDatabase db) {
        this.db = db;
    }

    private Expenses getExpenses(Cursor cursor) {

        Expenses expenses = new Expenses();
        expenses.setId_expenses(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        expenses.setDate_expenses(new Date(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(1)))));
        expenses.setCost_expenses(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(2))));
        expenses.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return expenses;
    }

    private List<Expenses> get(String selection, String[] selectionArgs) {

        List<Expenses> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("expenses", null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getExpenses(cursor));
                    } while (cursor.moveToNext());
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private void removeUpdate(String sql) {

        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

    }

    @Override
    public Expenses findExpensesById(Integer id) {

        Log.d("myDB", "Expenses findExpensesById start");

        List<Expenses> list = get("id_expenses = ?", new String[]{String.valueOf(id)});

        Log.d("myDB", "Expenses findExpensesById end");

        return list.get(0);
    }

    @Override
    public List<Expenses> getAll() {
        Log.d("myDB", "Expenses getAll start");

        List<Expenses> list = get(null, null);

        Log.d("myDB", "Expenses getAll end");

        return list;
    }

    @Override
    public List<Expenses> getAllByCategory(Integer categoryId) {

        Log.d("myDB", "Expenses getAllByCategory start");

        List<Expenses> list = get("id_category = ?", new String[]{String.valueOf(categoryId)});

        Log.d("myDB", "Expenses getAllByCategory end");

        return list;
    }

    @Override
    public List<Expenses> getAllByDateForCategory(Date date, Integer categoryId) {

        Log.d("myDB", "Expenses getAllByDateForCategory start");

        Long int_date = date.getTime();
        List<Expenses> list = get("id_category = ? and date_expenses = ?", new String[]{String.valueOf(categoryId), String.valueOf(int_date)});

        Log.d("myDB", "Expenses getAllByDateForCategory end");

        return list;
    }

    @Override
    public void add(Expenses expenses) {

        Log.d("myDB", "Expenses add start");

        String sql = "INSERT INTO expenses (date_expenses, cost_expenses, id_category) VALUES(?, ?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindLong(1, expenses.getDate_expenses().getTime());
            statement.bindDouble(2, expenses.getCost_expenses());
            statement.bindLong(3, expenses.getId_category());
            statement.execute();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Expenses add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Expenses removeAll start");

        removeUpdate("DELETE FROM expenses;");

        Log.d("myDB", "Expenses removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Expenses removeById start");

        removeUpdate("DELETE FROM expenses WHERE id_expenses = " + id + ";");

        Log.d("myDB", "Expenses removeById end");

    }

    @Override
    public void updateById(Expenses expenses) {

        Log.d("myDB", "Expenses updateById start");

        Long long_date = expenses.getDate_expenses().getTime();

        String sql = "UPDATE expenses SET date_expenses = " +
                long_date + ", " +
                "cost_expenses = " + expenses.getCost_expenses() + ", " +
                "id_category = " + expenses.getId_category() + " " +
                "WHERE id_expenses = " + expenses.getId_expenses() + ";";
        removeUpdate(sql);

        Log.d("myDB", "Expenses updateById end");

    }
}