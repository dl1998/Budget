package com.android.budget.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.android.budget.R;
import com.android.budget.adapter.TabsFragmentAdapter;
import com.android.budget.fragments.FragmentExpensesList;
import com.android.budget.fragments.FragmentIncomesList;

import java.util.Calendar;

/**
 * Created by dl1998 on 04.11.17.
 */

public class IncomeExpensesInfoActivity extends AppCompatActivity {

    public Calendar selectedDate;

    private FragmentIncomesList fragmentIncomes;
    private FragmentExpensesList fragmentExpenses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses_info);

        Toolbar toolbar = findViewById(R.id.toolbar_income_expenses);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeExpensesInfoActivity.this.finish();
            }
        });

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        fragmentIncomes = (FragmentIncomesList) adapter.getFragment(0);
        fragmentExpenses = (FragmentExpensesList) adapter.getFragment(1);

        selectedDate = Calendar.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnDateChoose:
                openDateChooserDialog();
                break;
        }

        return true;
    }

    private void openDateChooserDialog() {
        View alertLayout = getLayoutInflater().inflate(R.layout.alert_dialog_date_choose, null);

        DatePicker datePicker = alertLayout.findViewById(R.id.datePicker);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        final AlertDialog dialog = alert.create();
        dialog.show();

        datePicker.init(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                @Override
                public void onDateChanged(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, monthOfYear);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dialog.dismiss();
                    fragmentIncomes.refreshAdapter();
                    fragmentExpenses.refreshAdapter();
                }
            });
    }
}
