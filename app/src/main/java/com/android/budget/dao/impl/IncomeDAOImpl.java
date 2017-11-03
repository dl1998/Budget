package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.IncomeDAO;
import com.android.budget.entity.Income;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public class IncomeDAOImpl implements IncomeDAO {

    private SQLiteDatabase db;

    private IncomeDAOImpl() {
    }

    public IncomeDAOImpl(SQLiteDatabase db) {
        this.db = db;
    }

    private Income getIncome(Cursor cursor) {

        Income income = new Income();
        income.setId_income(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        income.setDate_income(new Date(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(1)))));
        income.setCost_income(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(2))));
        income.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return income;
    }

    private List<Income> get(String selection, String[] selectionArgs) {

        List<Income> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("income", null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(getIncome(cursor));
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
    public Income findIncomeById(Integer id) {

        Log.d("myDB", "Income findIncomeById start");

        List<Income> list = get("id_income = ?", new String[]{String.valueOf(id)});

        Log.d("myDB", "Income findIncomeById end");

        return list.get(0);
    }

    @Override
    public List<Income> getAll() {

        Log.d("myDB", "Income getAll start");

        List<Income> list = get(null, null);

        Log.d("myDB", "Income getAll end");

        return list;
    }

    @Override
    public List<Income> getAllByAccount(Integer accountId) {

        Log.d("myDB", "Income getAllByAccount start");

        List<Income> list = get("id_account = ?", new String[]{String.valueOf(accountId)});

        Log.d("myDB", "Income getAllByAccount end");

        return list;
    }

    @Override
    public List<Income> getAllByDateForAccount(Date date, Integer accountId) {

        Log.d("myDB", "Income getAllByDateForAccount start");

        Long int_date = date.getTime();
        List<Income> list = get("id_account = ? and date_income = ?", new String[]{String.valueOf(accountId), String.valueOf(int_date)});

        Log.d("myDB", "Income getAllByDateForAccount end");

        return list;
    }

    @Override
    public void add(Income income) {

        Log.d("myDB", "Income add start");

        String sql = "INSERT INTO income (date_income, cost_income, id_account) VALUES(?, ?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindLong(1, income.getDate_income().getTime());
            statement.bindDouble(2, income.getCost_income());
            statement.bindLong(3, income.getId_account());
            statement.execute();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Income add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Income removeAll start");

        removeUpdate("DELETE FROM income;");

        Log.d("myDB", "Income removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Income removeById start");

        removeUpdate("DELETE FROM income WHERE id_income = " + id + ";");

        Log.d("myDB", "Income removeById end");

    }

    @Override
    public void updateById(Income income) {

        Log.d("myDB", "Income updateById start");

        Long long_date = income.getDate_income().getTime();

        String sql = "UPDATE income SET " +
                "date_income = " + long_date + ", " +
                "cost_income = " + income.getCost_income() + ", " +
                "id_account = " + income.getId_account() + " " +
                "WHERE id_income = " + income.getId_income() + ";";
        removeUpdate(sql);

        Log.d("myDB", "Income updateById end");

    }
}