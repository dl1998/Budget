package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.AccountDAO;
import com.android.budget.entity.Account;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dimal on 09.10.2017.
 */

public class AccountDAOImpl implements AccountDAO {

    private SQLiteDatabase db;

    private AccountDAOImpl(){}

    public AccountDAOImpl(SQLiteDatabase db) {
        this.db = db;
    }

    private Account getAccount(Cursor cursor) {

        Account account = new Account();
        account.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        account.setName_account(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        account.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
        account.setBalance(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(3))));

        return account;
    }

    private List<Account> get(String selection, String[] selectionArgs) {

        List<Account> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("account", null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Account account = getAccount(cursor);

                        list.add(account);
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
    public Account findAccountById(Integer id) {

        Log.d("myDB", "Account findAccountById start");

        List<Account> list = get("id_account = ?", new String[]{String.valueOf(id)});

        Log.d("myDB", "Account findAccountById end");

        return list.get(0);
    }

    @Override
    public List<Account> getAll() {

        Log.d("myDB", "Account getAll start");

        List<Account> list = get(null, null);

        Log.d("myDB", "Account getAll end");

        return list;
    }

    @Override
    public void add(Account account) {

        Log.d("myDB", "Account add start");

        String sql = "INSERT INTO account (name_account, id_currency, balance) VALUES(?, ?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindString(1, account.getName_account());
            statement.bindLong(2, account.getId_currency());
            statement.bindDouble(3, account.getBalance());
            statement.execute();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Account add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Account removeAll start");

        removeUpdate("DELETE FROM account;");

        Log.d("myDB", "Account removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Account removeById start");

        removeUpdate("DELETE FROM account WHERE id_account = " + id + ";");

        Log.d("myDB", "Account removeById end");

    }

    @Override
    public void updateById(Account account) {

        Log.d("myDB", "Account updateById start");

        String sql = "UPDATE account SET name_account = \"" + account.getName_account() + "\", " +
                "id_currency = " + account.getId_currency() + ", " +
                "balance = " + account.getBalance() + " WHERE id_account = " + account.getId_account() + ";";
        removeUpdate(sql);

        Log.d("myDB", "Account updateById end");

    }
}
