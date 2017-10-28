package com.android.budget.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.AccountSettingsActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.AccountArrayAdapter;
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
        lvAccount.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountArrayAdapter adapter = (AccountArrayAdapter) parent.getAdapter();
                AccountListModel model = (AccountListModel) adapter.getItem(position);
                selectedAccountId = model.getId();
                SharedPreferences.Editor editor = MainActivity.preferences.edit();
                editor.putInt("selectedAccount", model.getId());
                editor.apply();
                tvAccountSelected.setText("Account Selected: " + model.getName() + " (" +
                        model.getCurrency() + ")");
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

        final AccountArrayAdapter accountArrayAdapter = new AccountArrayAdapter(getActivity(), R.layout.row_layout_account, listModels);

        lvAccount.setAdapter(accountArrayAdapter);
        lvAccount.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvAccount.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                final int checkedCount = lvAccount.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                accountArrayAdapter.toogleSelection(position);
                mode.invalidate();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                AccountListModel selectedItem;
                SparseBooleanArray selected;

                switch (item.getItemId()) {
                    case R.id.btnChange:
                        selected = accountArrayAdapter.getSelectedIds();
                        selectedItem = accountArrayAdapter.getItem(selected.keyAt(0));

                        selectedAccountId = selectedItem.getId();
                        mode.finish();
                        openAccountSettingsWindow(selectedAccountId);
                        return true;
                    case R.id.btnRemove:
                        selected = accountArrayAdapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                selectedItem = accountArrayAdapter.getItem(selected.keyAt(i));
                                if (selectedAccountId.equals(selectedItem.getId())) {
                                    MainActivity.preferences.edit().remove("selectedAccount").apply();
                                    tvAccountSelected.setText("Account Not Selected");
                                }
                                accountArrayAdapter.remove(selectedItem);
                                accountDAO.removeById(selectedItem.getId());
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.account_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                accountArrayAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem itemEdit = menu.findItem(R.id.btnChange);

                if (accountArrayAdapter.getSelectedCount() == 1) {
                    itemEdit.setVisible(true);
                    return true;
                } else {
                    itemEdit.setVisible(false);
                    return true;
                }
            }

        });
    }
}
