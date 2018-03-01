package com.android.budget.dao;

import java.util.List;

/**
 * Created by dl1998 on 01.03.18.
 */

public interface StandardDAO<T> {

    void add(T object, String SQL);

    void update(T object, String SQL);

    void remove(String SQL);

    List<T> get(String table, String selection, String[] selectionArgs, String orderBy);

}
