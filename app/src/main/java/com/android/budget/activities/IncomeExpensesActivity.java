package com.android.budget.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.budget.Converter;
import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.dao.impl.ExpensesDAOImpl;
import com.android.budget.dao.impl.IncomeDAOImpl;
import com.android.budget.entity.Account;
import com.android.budget.entity.Category;
import com.android.budget.entity.Expenses;
import com.android.budget.entity.Income;
import com.android.budget.fragments.FragmentCalculation;
import com.android.budget.fragments.FragmentSelectCategory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dimal on 18.10.2017.
 */

public class IncomeExpensesActivity extends AppCompatActivity {

    private CoordinatorLayout rootLayout;
    private ImageButton btnBackspace;
    private TextView tvSelectedDate;
    private TextView tvCost;
    private Button btnSpecialAction;

    private SimpleDateFormat dateFormat;
    private Calendar selectedDate;
    private String operationType;
    private Integer selectedAccountId;
    private Integer selectedCategoryId;
    private Integer selectedElementId;
    private Float startCost;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private Account account;
    private AccountDAOImpl accountDAO;
    private Category category;
    private CategoryDAOImpl categoryDAO;
    private Income income;
    private IncomeDAOImpl incomeDAO;
    private Expenses expenses;
    private ExpensesDAOImpl expensesDAO;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentCalculation fragmentCalculation;
    private FragmentSelectCategory fragmentSelectCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expenses);

        rootLayout = findViewById(R.id.income_expenses_layout);
        btnBackspace = findViewById(R.id.btnBackspace);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvCost = findViewById(R.id.tvCost);
        btnSpecialAction = findViewById(R.id.btnSpecialAction);

        operationType = getIntent().getExtras().getString("operationType");
        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);
        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        selectedElementId = getIntent().getIntExtra("elementId", -1);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentCalculation = new FragmentCalculation();
        fragmentTransaction.replace(R.id.income_expenses_fragment, fragmentCalculation);
        fragmentTransaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(operationType);
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

        initData();
    }

    public void initData() {

        Float cost;
        Date date;

        if (selectedElementId != -1) {
            if (operationType.equals(getString(R.string.income))) {
                incomeDAO = new IncomeDAOImpl(db);
                income = incomeDAO.findIncomeById(selectedElementId);

                cost = income.getCost_income();
                date = income.getDate_income();
            } else {
                expensesDAO = new ExpensesDAOImpl(db);
                expenses = expensesDAO.findExpensesById(selectedElementId);

                cost = expenses.getCost_expenses();
                date = expenses.getDate_expenses();
            }

            startCost = cost;
            tvCost.setText(String.valueOf(cost));
            selectedDate = Converter.getCalendar(date);
            tvSelectedDate.setText(dateFormat.format(date));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        accountDAO = new AccountDAOImpl(db);
        account = accountDAO.findAccountById(selectedAccountId);

        addUpdate();
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

    private void addUpdate() {
        if (operationType.equals(getString(R.string.income))) {
            if (selectedElementId != -1) updateIncome();
            else addIncome();
        } else {
            if (selectedElementId != -1) updateExpenses();
            else addExpenses();
        }
    }

    private void updateExpenses() {
        ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
        Expenses expenses = expensesDAO.findExpensesById(selectedElementId);


    }

    private void updateIncome() {
        btnSpecialAction.setText(getText(R.string.add) + " \"" + account.getName_account() + "\"");
        btnSpecialAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float cost = getDigit();
                if (cost <= 0F) {
                    Snackbar.make(rootLayout, "Income cannot be 0 or less!", Snackbar.LENGTH_LONG).show();
                } else {

                    income.setDate_income(Converter.getDate(selectedDate.getTime()));
                    income.setCost_income(cost);
                    income.setId_account(selectedAccountId);

                    startCost = income.getCost_income() - startCost;

                    Float balance = account.getBalance() + startCost;
                    balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

                    account.setBalance(balance);
                    accountDAO.updateById(account);

                    incomeDAO.updateById(income);

                    IncomeExpensesActivity.this.finish();
                }
            }
        });
    }

    private void addExpenses() {

        categoryDAO = new CategoryDAOImpl(db);

        if (selectedCategoryId != -1) {
            category = categoryDAO.findCategoryById(selectedCategoryId);

            btnSpecialAction.setText(getString(R.string.add) + " \"" + category.getName_category() + "\"");
            btnSpecialAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Float cost = getDigit();
                    if (cost <= 0F) {
                        Snackbar.make(rootLayout, "Expenses cannot be 0 or less!", Snackbar.LENGTH_LONG).show();
                    } else {

                        Expenses expenses = new Expenses();
                        expenses.setDate_expenses(new Date(selectedDate.getTimeInMillis()));
                        expenses.setCost_expenses(cost);
                        expenses.setId_category(selectedCategoryId);

                        Float balance = account.getBalance() - expenses.getCost_expenses();
                        balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

                        account.setBalance(balance);
                        accountDAO.updateById(account);

                        ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
                        expensesDAO.add(expenses);

                        IncomeExpensesActivity.this.finish();
                    }
                }
            });
        } else {
            btnSpecialAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Float cost = getDigit();

                    if (cost <= 0F) {
                        Snackbar.make(rootLayout, "Expenses cannot be 0 or less!", Snackbar.LENGTH_LONG).show();
                    } else {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentSelectCategory = new FragmentSelectCategory();

                        Bundle bundle = new Bundle();
                        bundle.putFloat("cost", cost);

                        fragmentSelectCategory.setArguments(bundle);
                        fragmentTransaction.replace(R.id.income_expenses_fragment, fragmentSelectCategory);
                        fragmentTransaction.commit();

                        btnSpecialAction.setText(R.string.add_category);

                        btnSpecialAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(IncomeExpensesActivity.this, CategorySettingsActivity.class);
                                Integer id = null;
                                intent.putExtra("categoryId", id);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
        }
    }

    private void addIncome() {
        btnSpecialAction.setText(getText(R.string.add) + " \"" + account.getName_account() + "\"");
        btnSpecialAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float cost = getDigit();
                if (cost <= 0F) {
                    Snackbar.make(rootLayout, "Income cannot be 0 or less!", Snackbar.LENGTH_LONG).show();
                } else {

                    income = new Income();
                    income.setDate_income(new Date(selectedDate.getTimeInMillis()));
                    income.setCost_income(cost);
                    income.setId_account(selectedAccountId);

                    Float balance = account.getBalance() + income.getCost_income();
                    balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

                    account.setBalance(balance);
                    accountDAO.updateById(account);

                    incomeDAO = new IncomeDAOImpl(db);
                    incomeDAO.add(income);

                    IncomeExpensesActivity.this.finish();
                }
            }
        });
    }

    public Float getDigit() {
        String digit = String.valueOf(tvCost.getText());
        if (digit.isEmpty()) return 0F;
        else return Float.parseFloat(String.valueOf(tvCost.getText()));
    }

    public Date getDate() {
        return new Date(selectedDate.getTimeInMillis());
    }

    private void openAlertDialogDatePicker() {
        View alertLayout = getLayoutInflater().inflate(R.layout.date_choose_alert_dialog, null);

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

                        tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()) + "");
                        dialog.dismiss();

                    }
                });
    }
}
