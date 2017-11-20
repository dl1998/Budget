package com.android.budget.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.adapter.model.IncomesModel;
import com.android.budget.dao.impl.AccountDAOImpl;
import com.android.budget.dao.impl.IncomeDAOImpl;
import com.android.budget.entity.Account;

import java.util.List;

/**
 * Created by dl1998 on 05.11.17.
 */

public class IncomesAdapter extends ArrayAdapter<IncomesModel> {

    private Context context;
    private LayoutInflater inflater;
    private List<IncomesModel> incomesList;
    private SparseBooleanArray mSelectedItemsIds;

    public IncomesAdapter(Context context, int resource, List<IncomesModel> incomesList) {
        super(context, resource, incomesList);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.incomesList = incomesList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_child_view, null);

            holder.cvCost = view.findViewById(R.id.child_view_cost);
            holder.cvDate = view.findViewById(R.id.child_view_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.cvCost.getLayoutParams();
        params.setMarginStart(context.getResources().getDimensionPixelSize(R.dimen.incomes_left_margin));
        holder.cvCost.setLayoutParams(params);
        holder.cvCost.setText(incomesList.get(position).getCost_income() + " " + incomesList.get(position).getCurrency());
        holder.cvDate.setText(String.valueOf(incomesList.get(position).getDate_income()));

        return view;
    }

    @Override
    public void remove(IncomesModel object) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        IncomeDAOImpl incomeDAO = new IncomeDAOImpl(db);
        AccountDAOImpl accountDAO = new AccountDAOImpl(db);

        Account account = accountDAO.findAccountById(object.getId_account());
        account.setBalance(account.getBalance() - object.getCost_income());
        accountDAO.updateById(account);
        incomeDAO.removeById(object.getId_income());

        incomesList.remove(object);
        notifyDataSetChanged();
    }

    public List<IncomesModel> getIncomesList() {
        return incomesList;
    }

    public void toogleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class ViewHolder {
        TextView cvCost;
        TextView cvDate;
    }
}
