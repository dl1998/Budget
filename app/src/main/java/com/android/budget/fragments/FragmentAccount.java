package com.android.budget.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.AccountSettingsActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.MyArrayAdapter;
import com.android.budget.adapter.model.AccountListModel;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.entity.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimal on 08.10.2017.
 */

public class FragmentAccount extends Fragment {

    private ListView lvAccount;
    private FloatingActionButton fab;
    private TextView tvAccountSelected;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private AccountDAOImpl accountDAO;
    private CurrencyDAOImpl currencyDAO;
    private Integer selectedAccountId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_fragment, container, false);

        Toolbar toolbar;
        if (container != null) {
            toolbar = (Toolbar) container.getRootView().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.account);
        }

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        accountDAO = new AccountDAOImpl(db);
        currencyDAO = new CurrencyDAOImpl(db);

        selectedAccountId = null;
        tvAccountSelected = (TextView) view.findViewById(R.id.tvSelectedAccount);

        if (MainActivity.preferences != null) {
            selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);
            if (selectedAccountId != -1) {
                Account account = accountDAO.findAccountById(selectedAccountId);
                tvAccountSelected.setText("Account Selected: " + account.getName_account() + " (" +
                        currencyDAO.findCurrencyById(account.getId_currency()).getIso_name_currency() + ")");
            }
        }

        lvAccount = (ListView) view.findViewById(R.id.lvAccount);
        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyArrayAdapter adapter = (MyArrayAdapter) parent.getAdapter();
                AccountListModel model = (AccountListModel) adapter.getItem(position);
                selectedAccountId = model.getId();
                SharedPreferences.Editor editor = MainActivity.preferences.edit();
                editor.putInt("selectedAccount", model.getId());
                editor.apply();
                tvAccountSelected.setText("Account Selected: " + model.getName() + " (" +
                        model.getCurrency() + ")");
            }
        });
        registerForContextMenu(lvAccount);
        lvAccount.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.account_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        MyArrayAdapter adapter = (MyArrayAdapter) parent.getAdapter();
                        AccountListModel model = (AccountListModel) adapter.getItem(position);

                        switch (item.getItemId()) {
                            case R.id.btnChange:
                                selectedAccountId = model.getId();
                                openAccountSettingsWindow(selectedAccountId);
                                break;
                            case R.id.btnRemove:
                                Log.d("myDB", "SelectedId: " + selectedAccountId + "ModelId: " + model.getId());
                                if (selectedAccountId.equals(model.getId())){
                                    MainActivity.preferences.edit().remove("selectedAccount").apply();
                                    tvAccountSelected.setText("Account Not Selected");
                                }
                                accountDAO.removeById(model.getId());
                                loadAccountsInList();
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();

                return true;
            }
        });

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountSettingsWindow(null);
            }
        });

        loadAccountsInList();

        return view;
    }

    private void openAccountSettingsWindow(Integer id) {
        Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
        intent.putExtra("accountId", id);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAccountsInList();
    }

    private void loadAccountsInList() {

        List<Account> accounts = accountDAO.getAll();
        ArrayList<AccountListModel> listModels = new ArrayList<>();

        for (Account account : accounts) {
            listModels.add(new AccountListModel(db, account));
        }

        lvAccount.setAdapter(new MyArrayAdapter(getActivity(), listModels.toArray(new AccountListModel[listModels.size()])));
    }
}
