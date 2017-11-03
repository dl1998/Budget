package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.CurrencyDAO;
import com.android.budget.entity.Currency;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dimal on 10.10.2017.
 */

public class CurrencyDAOImpl implements CurrencyDAO {

    private SQLiteDatabase db;

    private CurrencyDAOImpl() {
    }

    public CurrencyDAOImpl(SQLiteDatabase db) {
        this.db = db;
    }

    private Currency getCurrency(Cursor cursor) {

        Currency currency = new Currency();
        currency.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        currency.setName_currency(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        currency.setIso_name_currency(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

        return currency;
    }

    private List<Currency> get(String selection, String[] selectionArgs) {

        List<Currency> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("currency", null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Currency currency = getCurrency(cursor);

                        list.add(currency);
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
    public Currency findCurrencyById(Integer id) {

        Log.d("myDB", "Currency findCurrencyById start");

        List<Currency> list = get("id_currency = ?", new String[]{String.valueOf(id)});

        Log.d("myDB", "Currency findCurrencyById end");

        return list.get(0);
    }

    @Override
    public Currency findCurrencyByName(String name) {

        Log.d("myDB", "Currency findCurrencyByName start");

        List<Currency> list = get("name_currency = ?", new String[]{name});

        Log.d("myDB", "Currency findCurrencyByName end");

        return list.get(0);
    }

    @Override
    public List<Currency> getAll() {

        Log.d("myDB", "Currency getAll start");

        List<Currency> list = get(null, null);

        Log.d("myDB", "Currency getAll end");

        return list;
    }

    @Override
    public void add(Currency currency) {
        Log.d("myDB", "Currency add start");

        String sql = "INSERT INTO currency (name_currency, iso_name_currency) VALUES(?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindString(1, currency.getName_currency());
            statement.bindString(2, currency.getIso_name_currency());
            statement.execute();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Currency add end");
    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Currency removeAll start");

        removeUpdate("DELETE FROM currency;");

        Log.d("myDB", "Currency removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Currency removeById start");

        removeUpdate("DELETE FROM currency WHERE id_currency = " + id + ";");

        Log.d("myDB", "Currency removeById end");

    }

    @Override
    public void updateById(Currency currency) {

        Log.d("myDB", "Currency updateById start");

        String sql = "UPDATE currency SET name_currency = \"" +
                currency.getName_currency() + "\", " +
                "iso_name_currency = \"" + currency.getIso_name_currency() + "\" " +
                "WHERE id_currency = " + currency.getId_currency() + ";";
        removeUpdate(sql);

        Log.d("myDB", "Currency updateById end");

    }
}