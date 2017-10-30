package com.android.budget.dao;

import com.android.budget.entity.Income;

import java.sql.Date;
import java.util.List;

/**
 * Created by dl1998 on 29.10.17.
 */

public interface IncomeDAO {
    Income findIncomeById(Integer id);

    List<Income> getAll();

    List<Income> getAllByAccount(Integer accountId);

    List<Income> getAllByDateForAccount(Date date, Integer accountId);

    void add(Income income);

    void removeAll();

    void removeById(Integer id);

    void updateById(Integer id, Income income);
}
