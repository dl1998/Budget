package com.android.budget.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.budget.dao.StandardDAO;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 01.03.18.
 */

public abstract class StandardDAOImpl<T> implements StandardDAO<T> {

    private SQLiteDatabase db;

    public StandardDAOImpl(SQLiteDatabase db) {
        this.db = db;
    }

    abstract T getByCursor(Cursor cursor);

    abstract SQLiteStatement bind(SQLiteStatement statement, T object, boolean update);

    @Override
    public void add(T object, String SQL) {

        SQLiteStatement statement = db.compileStatement(SQL);

        db.beginTransaction();
        try {
            statement = bind(statement, object, false);
            statement.executeInsert();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void update(T object, String SQL) {

        SQLiteStatement statement = db.compileStatement(SQL);

        db.beginTransaction();
        try {
            statement = bind(statement, object, true);
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void remove(String SQL) {

        SQLiteStatement statement = db.compileStatement(SQL);

        db.beginTransaction();
        try {
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public List<T> get(String table, String selection, String[] selectionArgs, String orderBy) {

        List<T> objectList = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {

            cursor = db.query(table, null, selection, selectionArgs, null, null, orderBy);

            if (cursor.moveToFirst()) {
                do {
                    objectList.add(getByCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            db.endTransaction();
        }

        return objectList;
    }

}
