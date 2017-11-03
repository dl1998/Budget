package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.android.budget.dao.CategoryDAO;
import com.android.budget.entity.Category;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dimal on 13.10.2017.
 */

public class CategoryDAOImpl implements CategoryDAO {

    private SQLiteDatabase db;

    private CategoryDAOImpl(){}

    public CategoryDAOImpl(SQLiteDatabase db){
        this.db = db;
    }

    private Category getCategory(Cursor cursor) {

        Category category = new Category();
        category.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
        category.setName_category(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        category.setSrc_image(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));
        category.setId_account(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(3))));

        return category;
    }

    private List<Category> get(String selection, String[] selectionArgs) {

        List<Category> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("category", null, selection, selectionArgs, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Category category = getCategory(cursor);

                        list.add(category);
                    } while (cursor.moveToNext());
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;

    }

    private void removeUpdate(String sql) {
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Category findCategoryById(Integer id) {
        Log.d("myDB", "Category findCategoryById start");

        List<Category> list = get("id_category = ?", new String[]{String.valueOf(id)});

        Log.d("myDB", "Category findCategoryById end");

        return list.get(0);
    }

    @Override
    public List<Category> getAll() {

        Log.d("myDB", "Category getAll start");

        List<Category> list = get(null, null);

        Log.d("myDB", "Category getAll end");

        return list;
    }

    @Override
    public List<Category> getAllByAccount(Integer accountId) {

        Log.d("myDB", "Category getAllByAccount start");

        List<Category> list = get("id_account = ?", new String[]{String.valueOf(accountId)});

        Log.d("myDB", "Category getAllByAccount end");

        return list;
    }

    @Override
    public void add(Category category) {

        Log.d("myDB", "Category add start");

        String sql = "INSERT INTO category (name_category, src_image, id_account) VALUES(?, ?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindString(1, category.getName_category());
            statement.bindLong(2, category.getSrc_image());
            statement.bindLong(3, category.getId_account());
            statement.execute();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Category add end");

    }

    @Override
    public void removeAll() {

        Log.d("myDB", "Category removeAll start");

        removeUpdate("DELETE FROM category;");

        Log.d("myDB", "Category removeAll end");
    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Category removeById start");

        removeUpdate("DELETE FROM category WHERE id_category = " + id + ";");

        Log.d("myDB", "Category removeById end");

    }

    @Override
    public void updateById(Category category) {

        Log.d("myDB", "Category updateById start");

        String sql = "UPDATE category SET name_category = \"" +
                category.getName_category() + "\", " +
                "src_image = " + category.getSrc_image() + ", " +
                "id_account = " + category.getId_account() + " " +
                "WHERE id_category = " + category.getId_category() + ";";
        removeUpdate(sql);

        Log.d("myDB", "Category updateById end");

    }
}
