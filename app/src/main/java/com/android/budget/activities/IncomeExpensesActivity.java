package com.android.budget.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.budget.R;
import com.android.budget.fragments.FragmentCalculation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dimal on 18.10.2017.
 */

public class IncomeExpensesActivity extends AppCompatActivity {

    private ImageButton btnBackspace;
    private TextView tvSelectedDate;
    private TextView tvCost;

    private SimpleDateFormat dateFormat;
    private Calendar selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses);

        btnBackspace = (ImageButton) findViewById(R.id.btnBackspace);
        tvSelectedDate = (TextView) findViewById(R.id.tvSelectedDate);
        tvCost = (TextView) findViewById(R.id.tvCost);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.income_expenses_fragment, new FragmentCalculation());
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("New activity");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomeExpensesActivity.this.finish();
            }
        });

        getResources().getConfiguration().setLocale(new Locale("pl", "PL"));
        selectedDate = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("pl", "PL"));

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvCost.getText().toString();
                tvCost.setText(text.substring(0, text.length() > 0 ? text.length() - 1 : text.length()));
            }
        });

        tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()) + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_date, menu);

        MenuItem btnDateChoose = menu.findItem(R.id.btnDateChoose);
        btnDateChoose.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                openAlertDialogDatePicker();

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void openAlertDialogDatePicker() {
        View alertLayout = getLayoutInflater().inflate(R.layout.date_choose_alert_dialog, null);

        DatePicker datePicker = (DatePicker) alertLayout.findViewById(R.id.datePicker);

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

                        tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()) + "");
                        dialog.dismiss();

                    }
                });
    }
}
