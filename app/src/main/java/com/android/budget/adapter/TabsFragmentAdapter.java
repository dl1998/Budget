package com.android.budget.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.android.budget.fragments.AbstractTabFragment;
import com.android.budget.fragments.FragmentExpensesList;
import com.android.budget.fragments.FragmentIncomesList;

/**
 * Created by dl1998 on 05.11.17.
 */

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private SparseArray<AbstractTabFragment> tabs;
    private Context context;

    public TabsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        initTabsMap();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    private void initTabsMap() {
        tabs = new SparseArray<>();
        tabs.put(0, FragmentIncomesList.getInstance(context));
        tabs.put(1, FragmentExpensesList.getInstance(context));
    }

    public AbstractTabFragment getFragment(int position) {
        return tabs.get(position);
    }

}
