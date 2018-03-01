package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.CurrencyDAO;
import com.android.budget.entity.Currency;

import java.util.List;

/**
 * Created by dimal on 10.10.2017.
 */

public class CurrencyDAOImpl extends StandardDAOImpl<Currency> implements CurrencyDAO {

    private String TABLE_NAME = "currency";

    public CurrencyDAOImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    Currency getByCursor(Cursor cursor) {
        Currency currency = new Currency();

        currency.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        currency.setName_currency(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        currency.setIso_name_currency(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

        return currency;
    }

    @Override
    SQLiteStatement bind(SQLiteStatement statement, Currency currency, boolean update) {

        statement.clearBindings();
        statement.bindString(1, currency.getName_currency());
        statement.bindString(2, currency.getIso_name_currency());
        if (update) statement.bindLong(3, currency.getId_currency());

        return statement;
    }

    @Override
    public Currency findCurrencyById(Integer id) {

        Log.d("myDB", "Currency findCurrencyById start");

        List<Currency> list = super.get(TABLE_NAME, "id_currency = ?", new String[]{String.valueOf(id)}, null);

        Log.d("myDB", "Currency findCurrencyById end");

        return list.get(0);
    }

    @Override
    public Currency findCurrencyByName(String name) {

        Log.d("myDB", "Currency findCurrencyByName start");

        List<Currency> list = super.get(TABLE_NAME, "name_currency = ?", new String[]{name}, null);

        Log.d("myDB", "Currency findCurrencyByName end");

        return list.get(0);
    }

    @Override
    public List<Currency> getAll() {

        Log.d("myDB", "Currency getAll start");

        List<Currency> list = super.get(TABLE_NAME, null, null, null);

        Log.d("myDB", "Currency getAll end");

        return list;
    }

    @Override
    public void add(Currency currency) {
        Log.d("myDB", "Currency add start");

        String SQL = String.format("INSERT INTO %s (name_currency, iso_name_currency) VALUES(?, ?);", TABLE_NAME);
        super.add(currency, SQL);

        Log.d("myDB", "Currency add end");
    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Currency removeAll start");

        super.remove(String.format("DELETE FROM %s;", TABLE_NAME));

        Log.d("myDB", "Currency removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Currency removeById start");

        super.remove(String.format("DELETE FROM %s WHERE id_currency = %d;", TABLE_NAME, id));

        Log.d("myDB", "Currency removeById end");

    }

    @Override
    public void updateById(Currency currency) {

        Log.d("myDB", "Currency updateById start");

        String SQL = String.format("UPDATE %s SET name_currency = ?, iso_name_currency = ? " +
            "WHERE id_currency = ?;", TABLE_NAME);
        super.update(currency, SQL);

        Log.d("myDB", "Currency updateById end");

    }
}