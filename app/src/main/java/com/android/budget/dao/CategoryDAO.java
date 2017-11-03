package com.android.budget.dao;

import com.android.budget.entity.Category;

import java.util.List;

/**
 * Created by dimal on 13.10.2017.
 */

public interface CategoryDAO {

    Category findCategoryById(Integer id);

    List<Category> getAll();

    List<Category> getAllByAccount(Integer accountId);

    void add(Category category);

    void removeAll();

    void removeById(Integer id);

    void updateById(Category category);

}
