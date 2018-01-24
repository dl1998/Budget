package com.android.budget.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimal on 07.10.2017.
 */

public class FragmentStatistic extends Fragment {

    PieChart chart;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceBundle){

        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        Toolbar toolbar;
        if (container != null) {
            toolbar = container.getRootView().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.statistics);
        }

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

        chart = view.findViewById(R.id.chart);

        chart.setHoleRadius(60f);
        chart.setRotationEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setDrawCenterText(false);
        Description description = new Description();
        description.setEnabled(false);
        chart.setDescription(description);

        Float[] dataObjectsY = {1f, 10f, 121f, 40f, 34f};
        String[] dataObjectsX = {"Jesicca", "Michel", "Alex", "Joe", "Barbara"};

        addData(dataObjectsY, dataObjectsX);

        return view;
    }

    public void addData(Float[] dataObjectsY, String[] dataObjectsX){

        //TODO Add correct data from db

        List<PieEntry> yEntries = new ArrayList<>();

        for(int i = 0; i < dataObjectsY.length; i++){
            yEntries.add(new PieEntry(dataObjectsY[i], dataObjectsX[i]));
        }

        PieDataSet set = new PieDataSet(yEntries, null);

        int[] array = getResources().getIntArray(R.array.colors);
        ArrayList<Integer> colors = new ArrayList<>();
        for (Integer color : array) {
            colors.add(color);

        }

        set.setColors(colors);
        set.setValueTextSize(20f);
        set.setValueTextColor(Color.BLACK);

        Legend legend = chart.getLegend();

        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(15f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate();
    }
}
