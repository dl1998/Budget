package com.android.budget.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.budget.R;
import com.android.budget.adapter.model.AccountListModel;

import java.util.List;

/**
 * Created by dimal on 10.10.2017.
 */

public class AccountArrayAdapter extends ArrayAdapter<AccountListModel> {

    private Context context;
    private LayoutInflater inflater;
    private List<AccountListModel> accountModelList;
    private SparseBooleanArray mSelectedItemsIds;

    public AccountArrayAdapter(Context context, int resourceId, List<AccountListModel> accountModels){
        super(context, resourceId, accountModels);

        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.accountModelList = accountModels;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        TextView name;
        TextView balance;
        TextView currency;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_layout_account, null);

            holder.name = view.findViewById(R.id.tvAccountName);
            holder.balance = view.findViewById(R.id.tvAccountBalance);
            holder.currency = view.findViewById(R.id.tvAccountCurrency);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(accountModelList.get(position).getName());
        holder.balance.setText(String.valueOf(accountModelList.get(position).getBalance()));
        holder.currency.setText(accountModelList.get(position).getCurrency());

        return view;
    }

    @Override
    public void remove(AccountListModel object){
        accountModelList.remove(object);
        notifyDataSetChanged();
    }

    public List<AccountListModel> getAccountModelList(){
        return accountModelList;
    }

    public void toogleSelection(int position){
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection(){
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value){
        if(value){
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds(){
        return mSelectedItemsIds;
    }
}
