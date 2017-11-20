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

    private Mode selectedMode;

    private CoordinatorLayout rootLayout;

    private ImageButton btnBackspace;
    private Button btnClear;
    private TextView tvSelectedDate;
    private TextView tvCost;
    private Button btnSpecialAction;

    private Calendar selectedDate;
    private String operationType;
    private Integer selectedAccountId;
    private Integer selectedCategoryId;
    private Integer selectedElementId;
    private Float startCost;

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

        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);
        initFromExtras();

        initView();

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        fragmentManager = getSupportFragmentManager();
        showCalculatorFragment();

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

        selectedDate = Calendar.getInstance();

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvCost.getText().toString();
                tvCost.setText(text.substring(0, text.length() > 0 ? text.length() - 1 : text.length()));
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentCalculation != null) {
                    fragmentCalculation.reset();
                }
            }
        });

        btnSpecialAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Float cost = getDigit();
                if (cost <= 0F) {
                    Snackbar.make(rootLayout, operationType + " " + getString(R.string.cost_incorrect_info), Snackbar.LENGTH_LONG).show();
                } else {
                    switch (selectedMode) {
                        case INCOME_ADD:
                            addIncome();
                            break;
                        case EXPENSES_ADD:
                            addExpenses();
                            break;
                        case INCOME_UPDATE:
                            updateIncome();
                            break;
                        case EXPENSES_UPDATE:
                            updateExpenses();
                            break;
                        case CATEGORY_ADD:
                            addCategory();
                            break;
                    }
                }
            }
        });

        tvSelectedDate.setText(Converter.getTextDate(selectedDate));

        initData();
        initMode();
    }

    private void initView() {
        rootLayout = findViewById(R.id.income_expenses_layout);
        btnBackspace = findViewById(R.id.btnBackspace);
        btnClear = findViewById(R.id.btnClear);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvCost = findViewById(R.id.tvCost);
        btnSpecialAction = findViewById(R.id.btnSpecialAction);
    }

    private void initFromExtras() {
        operationType = getIntent().getStringExtra("operationType");
        selectedCategoryId = getIntent().getIntExtra("categoryId", -1);
        selectedElementId = getIntent().getIntExtra("elementId", -1);
    }

    private void initMode() {
        if (operationType.equals(getString(R.string.income))) {
            btnSpecialAction.setText(getText(R.string.add) + " \"" + account.getName_account() + "\"");
            if (selectedElementId != -1) selectedMode = Mode.INCOME_UPDATE;
            else selectedMode = Mode.INCOME_ADD;
        } else {
            categoryDAO = new CategoryDAOImpl(db);
            if (selectedCategoryId != -1) {
                category = categoryDAO.findCategoryById(selectedCategoryId);
                btnSpecialAction.setText(getString(R.string.add) + " \"" + category.getName_category() + "\"");
            }
            if (selectedElementId != -1) selectedMode = Mode.EXPENSES_UPDATE;
            else selectedMode = Mode.EXPENSES_ADD;
        }
    }

    private void showCalculatorFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentCalculation = new FragmentCalculation();
        fragmentTransaction.replace(R.id.income_expenses_fragment, fragmentCalculation);
        fragmentTransaction.commit();
    }

    public void initData() {
        Float cost;
        Date date;

        accountDAO = new AccountDAOImpl(db);
        account = accountDAO.findAccountById(selectedAccountId);

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("pl", "PL"));

            startCost = cost;
            tvCost.setText(String.valueOf(cost));
            selectedDate = Converter.getCalendar(date);
            tvSelectedDate.setText(dateFormat.format(date));
        }
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

    private void updateAccountBalance() {
        Float balance = account.getBalance() + startCost;
        balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

        account.setBalance(balance);
        accountDAO.updateById(account);
    }

    private void updateExpenses() {
        expenses.setDate_expenses(getDate());
        expenses.setCost_expenses(getDigit());

        startCost = expenses.getCost_expenses() - startCost;

        updateAccountBalance();

        expensesDAO.updateById(expenses);

        IncomeExpensesActivity.this.finish();
    }

    private void updateIncome() {
        income.setDate_income(getDate());
        income.setCost_income(getDigit());

        startCost = income.getCost_income() - startCost;

        updateAccountBalance();

        incomeDAO.updateById(income);

        IncomeExpensesActivity.this.finish();

    }

    private void addExpenses() {

        if (selectedCategoryId != -1) {

            Expenses expenses = new Expenses();
            expenses.setDate_expenses(new Date(selectedDate.getTimeInMillis()));
            expenses.setCost_expenses(getDigit());
            expenses.setId_category(selectedCategoryId);

            Float balance = account.getBalance() - expenses.getCost_expenses();
            balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

            account.setBalance(balance);
            accountDAO.updateById(account);

            ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
            expensesDAO.add(expenses);

            IncomeExpensesActivity.this.finish();

        } else {

            Bundle bundle = new Bundle();
            bundle.putFloat("cost", getDigit());

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentSelectCategory = new FragmentSelectCategory();
            fragmentSelectCategory.setArguments(bundle);
            fragmentTransaction.replace(R.id.income_expenses_fragment, fragmentSelectCategory);
            fragmentTransaction.commit();

            btnSpecialAction.setText(R.string.add_category);
            selectedMode = Mode.CATEGORY_ADD;
        }
    }

    private void addIncome() {
        income = new Income();
        income.setDate_income(new Date(selectedDate.getTimeInMillis()));
        income.setCost_income(getDigit());
        income.setId_account(selectedAccountId);

        Float balance = account.getBalance() + income.getCost_income();
        balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

        account.setBalance(balance);
        accountDAO.updateById(account);

        incomeDAO = new IncomeDAOImpl(db);
        incomeDAO.add(income);

        IncomeExpensesActivity.this.finish();
    }

    private void addCategory() {
        Intent intent = new Intent(IncomeExpensesActivity.this, CategorySettingsActivity.class);
        Integer id = null;
        intent.putExtra("categoryId", id);
        startActivity(intent);
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

                        tvSelectedDate.setText(Converter.getTextDate(selectedDate));
                        dialog.dismiss();
                    }
                });
    }

    private enum Mode {
        INCOME_ADD, EXPENSES_ADD, INCOME_UPDATE, EXPENSES_UPDATE, CATEGORY_ADD
    }
}