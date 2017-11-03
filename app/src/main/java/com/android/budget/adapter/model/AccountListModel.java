package com.android.budget.adapter.model;

import android.database.sqlite.SQLiteDatabase;

import com.android.budget.dao.CurrencyDAO;
import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.entity.Account;

/**
 * Created by dimal on 10.10.2017.
 */

public class AccountListModel {

    private Integer id;
    private String name;
    private Float balance;
    private String currency;

    public AccountListModel(SQLiteDatabase db, Account account){
        CurrencyDAO currencyDAO = new CurrencyDAOImpl(db);

        this.id = account.getId_account();
        this.name = account.getName_account();
        this.balance = account.getBalance();
        this.currency = currencyDAO.findCurrencyById(account.getId_currency()).getIso_name_currency();
    }

    public AccountListModel(String name, Float balance, String currency) {
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

}
