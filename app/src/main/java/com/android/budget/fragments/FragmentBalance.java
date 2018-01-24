package com.android.budget.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.CategorySettingsActivity;
import com.android.budget.activities.IncomeExpensesActivity;
import com.android.budget.activities.IncomeExpensesInfoActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.CategoriesAdapter;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.dao.impl.ExpensesDAOImpl;
import com.android.budget.entity.Account;
import com.android.budget.entity.Category;
import com.android.budget.entity.Expenses;

import java.util.ArrayList;

/**
 * Created by dimal on 08.10.2017.
 */

public class FragmentBalance extends Fragment implements View.OnTouchListener, View.OnClickListener{

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private CategoryDAOImpl categoryDAO;

    private Integer selectedAccountId;

    private GridView gridViewCategories;
    private ImageButton btnPlus;
    private ImageButton btnMinus;
    private Button btnBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);

        if (selectedAccountId == -1) {
            View contentView = view.findViewById(R.id.balance_Fragment_Content_View);
            contentView.setEnabled(false);
            contentView.setVisibility(View.INVISIBLE);
            CoordinatorLayout rootView = view.findViewById(R.id.balance_Root_View);
            inflater.inflate(R.layout.fragment_not_selected_account, rootView);
        }

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        categoryDAO = new CategoryDAOImpl(db);

        btnPlus = view.findViewById(R.id.btnAdd);
        btnMinus = view.findViewById(R.id.btnSubtract);
        gridViewCategories = view.findViewById(R.id.gvCircleCategories);
        btnBalance = view.findViewById(R.id.tvBalance);

        Toolbar toolbar;
        if (container != null) {
            toolbar = container.getRootView().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.balance);
        }

        btnPlus.setOnTouchListener(this);
        btnMinus.setOnTouchListener(this);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        btnBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), IncomeExpensesInfoActivity.class);
                startActivity(intent);
            }
        });

        gridViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoriesAdapter adapter = (CategoriesAdapter) parent.getAdapter();
                Category model = (Category) adapter.getItem(position);

                if(position == (adapter.getCount() - 1)){
                    openCategorySettingWindow(null);
                } else {
                    openIncomeExpensesWindow(model.getId_category(), getString(R.string.expenses));
                }
            }
        });

        gridViewCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                CategoriesAdapter adapter = (CategoriesAdapter) adapterView.getAdapter();

                if (position == (adapter.getCount() - 1)) return false;

                Category category = (Category) adapter.getItem(position);
                openModeDialog(category);
                return true;
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (selectedAccountId != -1) {
            loadListOfCategories();
            AccountDAOImpl accountDAO = new AccountDAOImpl(db);
            btnBalance.setText(getString(R.string.balance) + ": " + accountDAO.findAccountById(selectedAccountId).getBalance());
        }
    }

    public void openCategorySettingWindow(Integer categoryId) {
        Intent intent = new Intent(getActivity(), CategorySettingsActivity.class);

        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    public void openIncomeExpensesWindow(Integer categoryId, String operationType) {
        Intent intent = new Intent(getActivity(), IncomeExpensesActivity.class);

        intent.putExtra("operationType", operationType);
        if (categoryId != null) intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    private void openModeDialog(final Category category) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(category.getName_category());
        builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openCategorySettingWindow(category.getId_category());
            }
        });
        builder.setNegativeButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeCategory(category);
                loadListOfCategories();
                AccountDAOImpl accountDAO = new AccountDAOImpl(db);
                btnBalance.setText(getString(R.string.balance) + ": " + accountDAO.findAccountById(selectedAccountId).getBalance());
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void removeCategory(Category category) {
        ExpensesDAOImpl expensesDAO = new ExpensesDAOImpl(db);
        ArrayList<Expenses> expensesList = new ArrayList<>(expensesDAO.getAllByCategory(category.getId_category()));
        AccountDAOImpl accountDAO = new AccountDAOImpl(db);
        Account account = accountDAO.findAccountById(selectedAccountId);
        Float cost = 0F;
        for (Expenses expenses : expensesList) {
            cost += expenses.getCost_expenses();
            expensesDAO.removeById(expenses.getId_expenses());
        }
        account.setBalance(account.getBalance() + cost);
        accountDAO.updateById(account);
        categoryDAO.removeById(category.getId_category());
    }

    /**
     * Load all categories from selected account and add it in list
     */
    private void loadListOfCategories() {
        ArrayList<Category> categories = new ArrayList<>(categoryDAO.getAllByAccount(selectedAccountId));

        Category category = new Category();
        category.setId_category(categories.size());
        category.setSrc_image(R.drawable.plus);
        category.setName_category("Add");

        categories.add(category);

        CategoriesAdapter adapter = new CategoriesAdapter(getContext(),
                categories.toArray(new Category[categories.size()]), true);

        gridViewCategories.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.on_touch_button_pressed));
                break;
            case MotionEvent.ACTION_UP:
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.on_touch_button_released));
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.btnSubtract:
                openIncomeExpensesWindow(null, getString(R.string.expenses));
                break;
            case R.id.btnAdd:
                openIncomeExpensesWindow(null, getString(R.string.income));
                break;
        }
    }
}
