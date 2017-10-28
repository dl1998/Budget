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

    @Override
    public Account findAccountById(Integer id) {

        Log.d("myDB", "Account findAccountById start");

        Account account = null;

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("account", null, "id_account = ?", new String[]{id.toString()}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                account = new Account();
                account.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
                account.setName_account(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                account.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
                account.setBalance(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(3))));

                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("myDB", "Account findAccountById end");

        return account;
    }

    @Override
    public List<Account> getAll() {

        Log.d("myDB", "Account getAll start");

        List<Account> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("account", null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Account account = new Account();
                        account.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
                        account.setName_account(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                        account.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
                        account.setBalance(cursor.getLong(cursor.getColumnIndex(cursor.getColumnName(3))));

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
            statement.bindLong(3, account.getBalance());
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

        String sql = "DELETE FROM account;";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Account removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Account removeById start");

        String sql = "DELETE FROM account WHERE id_account = " + id + ";";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Account removeById end");

    }

    @Override
    public void removeByName(String name) {
        Log.d("myDB", "Account removeByName start");

        String sql = "DELETE FROM account WHERE account_name = \"" + name + "\";";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Account removeByName end");
    }

    @Override
    public void updateById(Integer id, Account account) {

        Log.d("myDB", "Account updateById start");

        String sql = "UPDATE account SET name_account = \"" + account.getName_account() + "\", " +
                "id_currency = " + account.getId_currency() + ", " +
                "balance = " + account.getBalance() + " WHERE id_account = " + id + ";";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Account updateById end");

    }

    public void closeDB(){
        db.close();
    }
}
