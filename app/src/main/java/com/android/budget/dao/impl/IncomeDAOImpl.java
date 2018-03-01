package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.Converter;
import com.android.budget.dao.IncomeDAO;
import com.android.budget.entity.Income;

import java.sql.Date;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public class IncomeDAOImpl extends StandardDAOImpl<Income> implements IncomeDAO {

    private String TABLE_NAME = "income";

    public IncomeDAOImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    Income getByCursor(Cursor cursor) {
        Income income = new Income();

        income.setId_income(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        income.setDate_income(Converter.getDate(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)))));
        income.setCost_income(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(2))));
        income.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return income;
    }

    @Override
    SQLiteStatement bind(SQLiteStatement statement, Income income, boolean update) {

        statement.clearBindings();
        statement.bindString(1, Converter.getTextDate(income.getDate_income()));
        statement.bindDouble(2, income.getCost_income());
        statement.bindLong(3, income.getId_account());
        if (update) statement.bindLong(4, income.getId_income());

        return statement;
    }

    @Override
    public Income findIncomeById(Integer id) {

        Log.d("myDB", "Income findIncomeById start");

        List<Income> list = super.get(TABLE_NAME, "id_income = ?", new String[]{String.valueOf(id)}, null);

        Log.d("myDB", "Income findIncomeById end");

        return list.get(0);
    }

    @Override
    public List<Income> getAll() {

        Log.d("myDB", "Income getAll start");

        List<Income> list = super.get(TABLE_NAME, null, null, null);

        Log.d("myDB", "Income getAll end");

        return list;
    }

    @Override
    public List<Income> getAllByAccount(Integer accountId) {

        Log.d("myDB", "Income getAllByAccount start");

        List<Income> list = super.get(TABLE_NAME, "id_account = ?", new String[]{String.valueOf(accountId)}, null);

        Log.d("myDB", "Income getAllByAccount end");

        return list;
    }

    @Override
    public List<Income> getAllByDateForAccount(Date date, Integer accountId) {

        Log.d("myDB", "Income getAllByDateForAccount start");

        List<Income> list = super.get(TABLE_NAME, "id_account = ? and date_income = ?",
            new String[]{String.valueOf(accountId), Converter.getTextDate(date)}, null);

        Log.d("myDB", "Income getAllByDateForAccount end");

        return list;
    }

    @Override
    public void add(Income income) {

        Log.d("myDB", "Income add start");

        String SQL = String.format("INSERT INTO %s (date_income, cost_income, id_account) VALUES(?, ?, ?);", TABLE_NAME);
        super.add(income, SQL);

        Log.d("myDB", "Income add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Income removeAll start");

        super.remove(String.format("DELETE FROM %s;", TABLE_NAME));

        Log.d("myDB", "Income removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Income removeById start");

        super.remove(String.format("DELETE FROM %s WHERE id_income = %d;", TABLE_NAME, id));

        Log.d("myDB", "Income removeById end");

    }

    @Override
    public void updateById(Income income) {

        Log.d("myDB", "Income updateById start");

        String SQL = String.format("UPDATE %s SET date_income = ?, cost_income = ?, id_account = ? " +
            "WHERE id_income = ?;", TABLE_NAME);
        super.update(income, SQL);

        Log.d("myDB", "Income updateById end");

    }
}