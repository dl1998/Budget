package com.android.budget.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.budget.R;
import com.android.budget.adapter.TabsFragmentAdapter;

/**
 * Created by dl1998 on 04.11.17.
 */

public class IncomeExpensesInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses_info);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }
}
