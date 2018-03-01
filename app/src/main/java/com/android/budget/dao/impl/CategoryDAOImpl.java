package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.CategoryDAO;
import com.android.budget.entity.Category;

import java.util.List;

/**
 * Created by dimal on 13.10.2017.
 */

public class CategoryDAOImpl extends StandardDAOImpl<Category> implements CategoryDAO {

    private String TABLE_NAME = "category";

    public CategoryDAOImpl(SQLiteDatabase db){
        super(db);
    }

    @Override
    Category getByCursor(Cursor cursor) {
        Category category = new Category();

        category.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        category.setName_category(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        category.setSrc_image(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
        category.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return category;
    }

    @Override
    SQLiteStatement bind(SQLiteStatement statement, Category category, boolean update) {

        statement.clearBindings();

        statement.bindString(1, category.getName_category());
        statement.bindLong(2, category.getSrc_image());
        statement.bindLong(3, category.getId_account());
        if (update) statement.bindLong(4, category.getId_category());

        return statement;
    }

    @Override
    public Category findCategoryById(Integer id) {
        Log.d("myDB", "Category findCategoryById start");

        List<Category> list = super.get(TABLE_NAME, "id_category = ?", new String[]{String.valueOf(id)}, null);

        Log.d("myDB", "Category findCategoryById end");

        return list.get(0);
    }

    @Override
    public List<Category> getAll() {

        Log.d("myDB", "Category getAll start");

        List<Category> list = super.get(TABLE_NAME, null, null, null);

        Log.d("myDB", "Category getAll end");

        return list;
    }

    @Override
    public List<Category> getAllByAccount(Integer accountId) {

        Log.d("myDB", "Category getAllByAccount start");

        List<Category> list = super.get(TABLE_NAME, "id_account = ?", new String[]{String.valueOf(accountId)}, null);

        Log.d("myDB", "Category getAllByAccount end");

        return list;
    }

    @Override
    public void add(Category category) {

        Log.d("myDB", "Category add start");

        String SQL = String.format("INSERT INTO %s (name_category, src_image, id_account) VALUES(?, ?, ?);", TABLE_NAME);
        super.add(category, SQL);

        Log.d("myDB", "Category add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Category removeAll start");

        super.remove(String.format("DELETE FROM %s;", TABLE_NAME));

        Log.d("myDB", "Category removeAll end");
    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Category removeById start");

        super.remove(String.format("DELETE FROM %s WHERE id_category = %d;", TABLE_NAME, id));

        Log.d("myDB", "Category removeById end");

    }

    @Override
    public void updateById(Category category) {

        Log.d("myDB", "Category updateById start");

        String SQL = String.format("UPDATE %s SET name_category = ?, src_image = ?, id_account = ? " +
            "WHERE id_category = ?;", TABLE_NAME);
        super.update(category, SQL);

        Log.d("myDB", "Category updateById end");

    }
}
