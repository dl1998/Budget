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

    @Override
    public Category findCategoryById(Integer id) {
        Log.d("myDB", "Category findCategoryById start");

        Category category = null;

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("category", null, "id_category = ?", new String[]{id.toString()}, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                category = new Category();
                category.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
                category.setName_category(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                category.setSrc_image(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));

                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("myDB", "Category findCategoryById end");

        return category;
    }

    @Override
    public List<Category> getAll() {

        Log.d("myDB", "Category getAll start");

        List<Category> list = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query("category", null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Category category = new Category();
                        category.setId_category(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0))));
                        category.setName_category(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                        category.setSrc_image(cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(2))));

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

        Log.d("myDB", "Category getAll end");

        return list;
    }

    @Override
    public void add(Category category) {

        Log.d("myDB", "Category add start");

        String sql = "INSERT INTO category (name_category, src_image) VALUES(?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.clearBindings();
            statement.bindString(1, category.getName_category());
            statement.bindLong(2, category.getSrc_image());
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

        String sql = "DELETE FROM category;";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Category removeAll end");
    }

    @Override
    public void removeById(Integer id) {

        Log.d("myDB", "Category removeById start");

        String sql = "DELETE FROM category WHERE id_category = " + id + ";";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Category removeById end");

    }

    @Override
    public void updateById(Integer id, Category category) {

        Log.d("myDB", "Category updateById start");

        String sql = "UPDATE category SET name_category = \"" +
                category.getName_category() + "\", " +
                "src_image = \"" + category.getSrc_image() + "\" " +
                "WHERE id_category = " + id + ";";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        Log.d("myDB", "Category updateById end");

    }
}
