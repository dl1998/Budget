package com.android.budget.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.activities.CategorySettingsActivity;
import com.android.budget.activities.IncomeExpensesActivity;
import com.android.budget.activities.MainActivity;
import com.android.budget.adapter.CategoriesAdapter;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.entity.Category;

import java.util.ArrayList;

/**
 * Created by dimal on 08.10.2017.
 */

public class FragmentBalance extends Fragment implements View.OnTouchListener, View.OnClickListener{

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private CategoryDAOImpl categoryDAO;

    private GridView gridViewCategories;
    private ImageButton btnPlus;
    private ImageButton btnMinus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.balance_fragment, container, false);

        if(MainActivity.preferences.getInt("selectedAccount", -1) == -1){
            View contentView = view.findViewById(R.id.balance_Fragment_Content_View);
            contentView.setEnabled(false);
            contentView.setVisibility(View.INVISIBLE);
            CoordinatorLayout rootView = view.findViewById(R.id.balance_Root_View);
            inflater.inflate(R.layout.not_selected_account_fragment, rootView);
        }

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        categoryDAO = new CategoryDAOImpl(db);

        btnPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        btnMinus = (ImageButton) view.findViewById(R.id.btnMinus);
        gridViewCategories = (GridView) view.findViewById(R.id.gridView);

        final NestedScrollView bottomSheetLayout = (NestedScrollView) view.findViewById(R.id.bottomSheetLayout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    gridViewCategories.setEnabled(false);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    gridViewCategories.setEnabled(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                gridViewCategories.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0)
                        .start();
            }
        });

        Toolbar toolbar;
        if (container != null) {
            toolbar = (Toolbar) container.getRootView().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.balance);
        }

        btnPlus.setOnTouchListener(this);
        btnMinus.setOnTouchListener(this);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        gridViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoriesAdapter adapter = (CategoriesAdapter) parent.getAdapter();
                Category model = (Category) adapter.getItem(position);

                if(position == (adapter.getCount() - 1)){
                    openCategorySettingWindow(null);
                } else {
                    openIncomeExpensesWindow(model.getId_category());
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadListOfCurrencies();
    }

    public void openCategorySettingWindow(Integer id){
        Intent intent = new Intent(getActivity(), CategorySettingsActivity.class);

        intent.putExtra("categoryId", id);
        startActivity(intent);
    }

    public void openIncomeExpensesWindow(Integer id){
        Intent intent = new Intent(getActivity(), IncomeExpensesActivity.class);

        intent.putExtra("categoryId", id);
        startActivity(intent);
    }

    /**
     * Load all currencies from selected account and add it in list
     */
    private void loadListOfCurrencies(){
        ArrayList<Category> categories = new ArrayList<>(categoryDAO.getAll());
        Category category = new Category();
        category.setId_category(categories.size());
        category.setSrc_image(R.drawable.plus);
        category.setName_category("Add");
        categories.add(category);
        CategoriesAdapter adapter = new CategoriesAdapter(getContext(),
                categories.toArray(new Category[categories.size()]));
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

        Intent intent = new Intent(getActivity(), IncomeExpensesActivity.class);

        switch (view.getId()){
            case R.id.btnMinus:
                startActivity(intent);
                break;
            case R.id.btnPlus:
                startActivity(intent);
                break;
        }
    }
}
