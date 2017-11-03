package com.android.budget.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.entity.Account;
import com.android.budget.entity.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimal on 14.10.2017.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private CurrencyDAOImpl currencyDAO;
    private Currency selectedCurrency;
    private AccountDAOImpl accountDAO;
    private Account account;

    private TextView tvSelectedCurrency;
    private MenuItem btnApply;
    private EditText etAccountName;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        Integer selectedAccountId = (Integer) getIntent().getExtras().getSerializable("accountId");
        coordinatorLayout = findViewById(R.id.account_settings_coordinator_layout);

        tvSelectedCurrency = findViewById(R.id.tvSelectedCurrency);
        etAccountName = findViewById(R.id.etAccountName);

        selectedCurrency = null;

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        currencyDAO = new CurrencyDAOImpl(db);
        accountDAO = new AccountDAOImpl(db);

        initializeData(selectedAccountId);

        Toolbar toolbar = findViewById(R.id.toolbar_account);
        toolbar.inflateMenu(R.menu.menu_apply);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSettingsActivity.this.finish();
            }
        });

    }

    public void initializeData(Integer selectedAccountId){
        if (selectedAccountId != null) {
            account = accountDAO.findAccountById(selectedAccountId);
            etAccountName.setText(account.getName_account(), TextView.BufferType.EDITABLE);
            selectedCurrency = currencyDAO.findCurrencyById(account.getId_currency());
            tvSelectedCurrency.setText(selectedCurrency.getIso_name_currency());
        } else {
            selectedCurrency = currencyDAO.findCurrencyByName("Euro");
            tvSelectedCurrency.setText(selectedCurrency.getIso_name_currency());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apply, menu);

        btnApply = menu.findItem(R.id.btnApply);
        btnApply.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (selectedCurrency != null && !etAccountName.getText().toString().isEmpty()) {

                    if (account == null) {
                        account = new Account();
                        account.setId_currency(selectedCurrency.getId_currency());
                        account.setName_account(etAccountName.getText().toString());
                        account.setBalance(0F);
                        accountDAO.add(account);
                    } else {
                        account.setId_currency(selectedCurrency.getId_currency());
                        account.setName_account(etAccountName.getText().toString());
                        accountDAO.updateById(account);
                    }

                    AccountSettingsActivity.this.finish();

                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etAccountName.getWindowToken(), 0);
                    Snackbar.make(coordinatorLayout, R.string.account_incorrect_info, Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void openCurrenciesChooser(View view) {
        View alertLayout = getLayoutInflater().inflate(R.layout.select_currency_alert_dialog, null);
        final ListView listCurrencies = alertLayout.findViewById(R.id.listCurrencies);

        List<Currency> currenciesList = currencyDAO.getAll();
        ArrayList<String> list = new ArrayList<>();
        for (Currency currency : currenciesList) {
            list.add(currency.getName_currency());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listCurrencies.setAdapter(adapter);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose Currency");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        listCurrencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = currencyDAO.findCurrencyByName(adapter.getItem(position));
                tvSelectedCurrency.setText(selectedCurrency.getIso_name_currency());
                dialog.dismiss();
            }
        });

    }

}
