package com.android.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.budget.R;
import com.android.budget.adapter.model.AccountListModel;

/**
 * Created by dimal on 10.10.2017.
 */

public class MyArrayAdapter extends BaseAdapter {

    private final Context context;
    private final AccountListModel[] accounts;
    private final LayoutInflater inflater;

    public MyArrayAdapter(Context context, AccountListModel[] accounts) {
        this.context = context;
        this.accounts = accounts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return accounts.length;
    }

    @Override
    public Object getItem(int position) {
        return accounts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row_layout_account, parent, false);
        }

        AccountListModel listModel = accounts[position];

        ((TextView) view.findViewById(R.id.tvAccountName)).setText(listModel.getName());
        ((TextView) view.findViewById(R.id.tvAccountBalance)).setText(listModel.getBalance().toString());
        ((TextView) view.findViewById(R.id.tvAccountCurrency)).setText(listModel.getCurrency());

        return view;
    }
}
