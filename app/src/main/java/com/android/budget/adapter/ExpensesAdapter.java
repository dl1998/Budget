package com.android.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.budget.R;
import com.android.budget.adapter.model.ExpensesModel;
import com.android.budget.entity.Expenses;

import java.util.ArrayList;

/**
 * Created by dl1998 on 04.11.17.
 */

public class ExpensesAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ExpensesModel> expensesModels;

    public ExpensesAdapter(Context context, ArrayList<ExpensesModel> expensesModels) {
        this.context = context;
        this.expensesModels = expensesModels;
    }

    @Override
    public int getGroupCount() {
        return expensesModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expensesModels.get(groupPosition).getExpensesList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expensesModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expensesModels.get(groupPosition).getExpensesList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_group_view, null);
        }

        if (isExpanded) {

        } else {

        }

        ImageView gvCategoryImage = convertView.findViewById(R.id.group_view_category_image);
        TextView gvCategoryName = convertView.findViewById(R.id.group_view_category_name);
        TextView gvExpensesCount = convertView.findViewById(R.id.group_view_expenses_count);
        TextView gvSummaryCost = convertView.findViewById(R.id.group_view_summary_cost);

        gvCategoryImage.setImageResource(expensesModels.get(groupPosition).getCategory().getSrc_image());
        gvCategoryName.setText(expensesModels.get(groupPosition).getCategory().getName_category());
        gvExpensesCount.setText(String.valueOf(expensesModels.get(groupPosition).getExpensesList().size()));
        gvSummaryCost.setText(getSummaryCost(groupPosition) + " " + expensesModels.get(groupPosition).getCurrency_ISO());

        return convertView;
    }

    private Float getSummaryCost(int groupPosition) {
        Float summaryCost = 0F;

        for (Expenses expenses : expensesModels.get(groupPosition).getExpensesList()) {
            summaryCost += expenses.getCost_expenses();
        }

        return summaryCost;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_child_view, null);
        }

        TextView cvExpensesCost = convertView.findViewById(R.id.child_view_cost);
        TextView cvExpensesDate = convertView.findViewById(R.id.child_view_date);

        cvExpensesCost.setText(expensesModels.get(groupPosition).getExpensesList().get(childPosition).getCost_expenses() +
                " " + expensesModels.get(groupPosition).getCurrency_ISO());
        cvExpensesDate.setText(String.valueOf(expensesModels.get(groupPosition).getExpensesList().get(childPosition).getDate_expenses()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
