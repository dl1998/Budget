package com.android.budget.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.IncomeExpensesActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.CategoriesAdapter;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.dao.impl.ExpensesDAOImpl;
import com.android.budget.entity.Account;
import com.android.budget.entity.Category;
import com.android.budget.entity.Expenses;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by dl1998 on 31.10.17.
 */

public class FragmentSelectCategory extends Fragment {

    private SQLiteDatabase db;
    private CategoryDAOImpl categoryDAO;

    private Integer selectedAccountId;

    private GridView gvSquareCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_category, container, false);

        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);

        gvSquareCategories = view.findViewById(R.id.gvSquareCategories);

        DBHelper dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        categoryDAO = new CategoryDAOImpl(db);

        final Float cost = getArguments().getFloat("cost");

        gvSquareCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cost > 0) {
                    CategoriesAdapter adapter = (CategoriesAdapter) parent.getAdapter();
                    Category model = (Category) adapter.getItem(position);

                    AccountDAOImpl accountDAO = new AccountDAOImpl(db);
                    Account account = accountDAO.findAccountById(selectedAccountId);

                    ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
                    Expenses expenses = new Expenses();

                    IncomeExpensesActivity incomeExpensesActivity = (IncomeExpensesActivity) getActivity();

                    expenses.setCost_expenses(cost);
                    expenses.setDate_expenses(incomeExpensesActivity.getDate());
                    expenses.setId_category(model.getId_category());

                    expensesDAO.add(expenses);

                    Float balance = account.getBalance() - expenses.getCost_expenses();
                    balance = new BigDecimal(balance).setScale(2, RoundingMode.CEILING).floatValue();

                    account.setBalance(balance);
                    accountDAO.updateById(account);

                    incomeExpensesActivity.finish();
                }
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        loadListOfCurrencies();
    }

    private void loadListOfCurrencies() {
        ArrayList<Category> categories = new ArrayList<>(categoryDAO.getAllByAccount(selectedAccountId));

        CategoriesAdapter adapter = new CategoriesAdapter(getContext(),
                categories.toArray(new Category[categories.size()]), false);

        gvSquareCategories.setAdapter(adapter);
    }
}
