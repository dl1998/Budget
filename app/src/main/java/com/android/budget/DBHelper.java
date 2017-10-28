package com.android.budget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.entity.Currency;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by dimal on 09.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final Integer DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "budgetDB";
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myDB", "onCreate Database Start");

        db.execSQL("create table account ( " +
                "id_account integer primary key autoincrement, " +
                "name_account text, " +
                "id_currency integer, " +
                "balance integer " +
                ");");

        db.execSQL("create table currency ( " +
                "id_currency integer primary key autoincrement, " +
                "name_currency text, " +
                "iso_name_currency text " +
                ");");

        db.execSQL("create table category ( " +
                "id_category integer primary key autoincrement, " +
                "name_category text, " +
                "src_image integer " +
                ");");

        db.execSQL("create table outgoing ( " +
                "id_outgoing integer primary key autoincrement, " +
                "date_outgoing integer, " +
                "cost_outgoing integer, " +
                "id_account integer, " +
                "id_category integer " +
                ");");

        db.execSQL("create table income ( " +
                "id_income integer primary key autoincrement, " +
                "date_income integer, " +
                "cost_income integer, " +
                "id_account integer " +
                ");");

        loadCurrencies(db);

        Log.d("myDB", "onCreate Database End");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void loadCurrencies(SQLiteDatabase db) {
        Resources res = context.getResources();

        CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl(db);

        try (XmlResourceParser _xml = res.getXml(R.xml.currencies)) {

            int eventType = _xml.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if ((eventType == XmlPullParser.START_TAG) && (_xml.getName().equals("record"))) {

                    Currency currency = new Currency();
                    currency.setName_currency(_xml.getAttributeValue(1));
                    currency.setIso_name_currency(_xml.getAttributeValue(0));
                    currencyDAO.add(currency);
                }
                eventType = _xml.next();
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e("myDB", e.getMessage(), e);
        }
    }
}
