package com.android.budget.dao;

import com.android.budget.entity.Currency;

import java.util.List;

/**
 * Created by dimal on 10.10.2017.
 */

public interface CurrencyDAO {

    Currency findCurrencyById(Integer id);
    Currency findCurrencyByName(String name);
    List<Currency> getAll();
    void add(Currency currency);
    void removeAll();
    void removeById(Integer id);
    void updateById(Integer id, Currency currency);

}
