package com.android.budget.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.budget.Converter;
import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.IncomeExpensesActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.IncomesAdapter;
import com.android.budget.adapter.model.IncomesModel;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.dao.impl.IncomeDAOImpl;
import com.android.budget.entity.Income;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by dl1998 on 04.11.17.
 */

public class FragmentIncomesList extends AbstractTabFragment {

    private Integer selectedAccountId;

    private ListView listView;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private AccountDAOImpl accountDAO;
    private CurrencyDAOImpl currencyDAO;
    private IncomeDAOImpl incomeDAO;

    public static FragmentIncomesList getInstance(Context context) {
        FragmentIncomesList fragmentIncomesList = new FragmentIncomesList();

        fragmentIncomesList.context = context;
        fragmentIncomesList.setArguments(new Bundle());
        fragmentIncomesList.setTitle(context.getString(R.string.income));

        return fragmentIncomesList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income_fragment, container, false);

        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);

        listView = view.findViewById(R.id.incomesList);

        initDAO();

        return view;
    }

    public void onResume() {
        super.onResume();

        loadIncomesInList();
    }

    private void initDAO() {
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        accountDAO = new AccountDAOImpl(db);
        currencyDAO = new CurrencyDAOImpl(db);
        incomeDAO = new IncomeDAOImpl(db);
    }

    private ArrayList<IncomesModel> getIncomesModels(Date date) {

        Integer currencyId = accountDAO.findAccountById(selectedAccountId).getId_currency();
        String currency = currencyDAO.findCurrencyById(currencyId).getIso_name_currency();

        ArrayList<Income> incomes = new ArrayList<>(incomeDAO.getAllByDateForAccount(Converter.getDate(date), selectedAccountId));

        ArrayList<IncomesModel> incomesModels = new ArrayList<>();

        for (Income income : incomes) {
            IncomesModel model = new IncomesModel(income, currency, selectedAccountId);
            incomesModels.add(model);
        }

        return incomesModels;
    }

    private void openIncomeExpensesSetting(Integer incomeId) {
        Intent intent = new Intent(getActivity(), IncomeExpensesActivity.class);
        intent.putExtra("elementId", incomeId);
        intent.putExtra("operationType", getString(R.string.income));

        startActivity(intent);
    }

    private void loadIncomesInList() {
        Date date = new Date(System.currentTimeMillis());
        final IncomesAdapter adapter = new IncomesAdapter(getActivity(), R.layout.child_view, getIncomesModels(Converter.getDate(date)));
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adapter.toogleSelection(position);
                mode.invalidate();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                IncomesModel selectedIncomesModel;
                SparseBooleanArray selected;

                switch (item.getItemId()) {
                    case R.id.btnChange:
                        selected = adapter.getSelectedIds();
                        selectedIncomesModel = adapter.getItem(selected.keyAt(0));

                        Integer selectedIncomeId = selectedIncomesModel.getId_income();
                        mode.finish();
                        openIncomeExpensesSetting(selectedIncomeId);
                        return true;
                    case R.id.btnRemove:
                        selected = adapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                selectedIncomesModel = adapter.getItem(selected.keyAt(i));
                                adapter.remove(selectedIncomesModel);
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
                adapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem itemEdit = menu.findItem(R.id.btnChange);

                if (adapter.getSelectedCount() == 1) {
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
