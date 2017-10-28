package com.android.budget.dao;

import com.android.budget.entity.Account;

import java.util.List;

/**
 * Created by dimal on 09.10.2017.
 */

public interface AccountDAO {

    Account findAccountById(Integer id);
    List<Account> getAll();
    void add(Account account);
    void removeAll();
    void removeById(Integer id);
    void removeByName(String name);
    void updateById(Integer id, Account account);

}
