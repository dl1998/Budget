package com.android.budget.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.budget.DBHelper;
import com.android.budget.R;
import com.android.budget.dao.impl.CategoryDAOImpl;
import com.android.budget.entity.Category;

/**
 * Created by dimal on 24.10.2017.
 */

public class CategorySettingsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText etCategoryName;
    private CoordinatorLayout coordinatorLayout;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private CategoryDAOImpl categoryDAO;
    private Category category;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_settings);

        Integer selectedCategoryId = (Integer) getIntent().getExtras().getSerializable("categoryId");

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        categoryDAO = new CategoryDAOImpl(db);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        etCategoryName = (EditText) findViewById(R.id.etCategoryName);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySettingsActivity.this.finish();
            }
        });

        initializeData(selectedCategoryId);

    }

    public void initializeData(Integer selectedCategoryId){
        if(selectedCategoryId == null){
            category = new Category();
            category.setSrc_image(R.drawable.coin);
        } else {
            category = categoryDAO.findCategoryById(selectedCategoryId);
            etCategoryName.setText(category.getName_category());
            fab.setImageResource(category.getSrc_image());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_apply, menu);

        MenuItem btnApply = menu.findItem(R.id.btnApply);
        btnApply.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (!etCategoryName.getText().toString().isEmpty()) {

                    if(category.getId_category() == null){
                        category.setName_category(etCategoryName.getText().toString());
                        categoryDAO.add(category);
                    } else {
                        categoryDAO.updateById(category.getId_category(), category);
                    }

                    CategorySettingsActivity.this.finish();

                } else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etCategoryName.getWindowToken(), 0);
                    Snackbar.make(coordinatorLayout, R.string.account_incorrect_info, Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
