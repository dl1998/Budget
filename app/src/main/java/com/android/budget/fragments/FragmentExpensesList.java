package com.android.budget.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.budget.Converter;
import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.ExpensesAdapter;
import com.android.budget.adapter.model.ExpensesModel;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.dao.impl.CurrencyDAOImpl;
import com.android.budget.entity.Category;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by dl1998 on 04.11.17.
 */

public class FragmentExpensesList extends AbstractTabFragment {

    private Integer selectedAccountId;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private CategoryDAOImpl categoryDAO;

    private ExpandableListView expandableListView;

    public static FragmentExpensesList getInstance(Context context) {
        FragmentExpensesList fragmentExpensesList = new FragmentExpensesList();

        fragmentExpensesList.context = context;
        fragmentExpensesList.setArguments(new Bundle());
        fragmentExpensesList.setTitle(context.getString(R.string.expenses));

        return fragmentExpensesList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);

        expandableListView = view.findViewById(R.id.expanded_list);

        initDAO();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ExpensesAdapter expensesAdapter = new ExpensesAdapter(getActivity(), getExpensesModels());
        expandableListView.setAdapter(expensesAdapter);
    }

    private void initDAO() {
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        categoryDAO = new CategoryDAOImpl(db);
    }

    private ArrayList<ExpensesModel> getExpensesModels() {
        ArrayList<Category> categories = new ArrayList<>(categoryDAO.getAllByAccount(selectedAccountId));

        AccountDAOImpl accountDAO = new AccountDAOImpl(db);
        CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl(db);
        Integer currencyId = accountDAO.findAccountById(selectedAccountId).getId_currency();
        String currencyISO = currencyDAO.findCurrencyById(currencyId).getIso_name_currency();

        ArrayList<ExpensesModel> expensesModels = new ArrayList<>();
        for (Category category : categories) {
            Date date = new Date(System.currentTimeMillis());
            ExpensesModel expensesModel = new ExpensesModel(db, currencyISO, category, Converter.getDate(date));
            if (expensesModel.getExpensesList().size() != 0) expensesModels.add(expensesModel);
        }

        return expensesModels;
    }
}
