package com.android.budget;

import android.util.Log;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dl1998 on 04.11.17.
 */

public class Converter {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getDate(String dateText) {
        Log.d("myDB", "String date: " + dateText);
        java.util.Date tempDate = null;
        try {
            tempDate = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(tempDate.getTime());
    }

    public static Date getDate(java.util.Date dateUtil) {
        java.util.Date tempDate = null;
        try {
            String temp = simpleDateFormat.format(dateUtil);
            tempDate = simpleDateFormat.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(tempDate.getTime());
    }

    public static String getTextDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
