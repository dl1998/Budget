package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.AccountDAO;
import com.android.budget.entity.Account;

import java.util.List;

/**
 * Created by dimal on 09.10.2017.
 */

public class AccountDAOImpl extends StandardDAOImpl<Account> implements AccountDAO {

    private String TABLE_NAME = "account";

    public AccountDAOImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    Account getByCursor(Cursor cursor) {
        Account account = new Account();

        account.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        account.setName_account(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        account.setId_currency(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
        account.setBalance(cursor.getFloat(cursor.getColumnIndex(cursor.getColumnName(3))));

        return account;
    }

    @Override
    SQLiteStatement bind(SQLiteStatement statement, Account account, boolean update) {

        statement.clearBindings();
        statement.bindString(1, account.getName_account());
        statement.bindLong(2, account.getId_currency());
        statement.bindDouble(3, account.getBalance());
        if (update) statement.bindLong(4, account.getId_account());

        return statement;
    }

    @Override
    public Account findAccountById(Integer id) {

        Log.d("myDB", "Account findAccountById start");

        List<Account> list = super.get(TABLE_NAME, "id_account = ?", new String[]{String.valueOf(id)}, null);

        Log.d("myDB", "Account findAccountById end");

        return list.get(0);
    }

    @Override
    public List<Account> getAll() {

        Log.d("myDB", "Account getAll start");

        List<Account> list = super.get(TABLE_NAME, null, null, null);

        Log.d("myDB", "Account getAll end");

        return list;
    }

    @Override
    public void add(Account account) {

        Log.d("myDB", "Account add start");

        String SQL = String.format("INSERT INTO %s (name_account, id_currency, balance) VALUES(?, ?, ?);", TABLE_NAME);
        super.add(account, SQL);

        Log.d("myDB", "Account add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Account removeAll start");

        super.remove(String.format("DELETE FROM %s;", TABLE_NAME));

        Log.d("myDB", "Account removeAll end");

    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Account removeById start");

        super.remove(String.format("DELETE FROM %s WHERE id_account = %d;", TABLE_NAME, id));

        Log.d("myDB", "Account removeById end");

    }

    @Override
    public void updateById(Account account) {

        Log.d("myDB", "Account updateById start");

        String SQL = String.format("UPDATE %s SET name_account = ?, id_currency = ?, balance = ? " +
            "WHERE id_account = ?;", TABLE_NAME);
        super.update(account, SQL);

        Log.d("myDB", "Account updateById end");

    }
}
