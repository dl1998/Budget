package com.android.budget.dao;

import com.android.budget.entity.Expenses;

import java.sql.Date;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public interface ExpensesDAO {
    Expenses findExpensesById(Integer id);

    List<Expenses> getAll();

    List<Expenses> getAllByCategory(Integer categoryId);

    List<Expenses> getAllByDateForCategory(Date date, Integer categoryId);

    void add(Expenses expenses);

    void removeAll();

    void removeById(Integer id);

    void updateById(Expenses expenses);
}
