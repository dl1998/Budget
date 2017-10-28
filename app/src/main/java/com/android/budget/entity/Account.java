package com.android.budget.entity;

/**
 * Created by dimal on 09.10.2017.
 */

public class Account {

    private Integer id_account;
    private String name_account;
    private Integer id_currency;
    private Long balance;

    public Account(){}

    public Account(Integer id_account, String name_account, Integer id_currency, Long balance) {
        this.id_account = id_account;
        this.name_account = name_account;
        this.id_currency = id_currency;
        this.balance = balance;
    }

    public Integer getId_account() {
        return id_account;
    }

    public void setId_account(Integer id_account) {
        this.id_account = id_account;
    }

    public String getName_account() {
        return name_account;
    }

    public void setName_account(String name_account) {
        this.name_account = name_account;
    }

    public Integer getId_currency() {
        return id_currency;
    }

    public void setId_currency(Integer id_currency) {
        this.id_currency = id_currency;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
