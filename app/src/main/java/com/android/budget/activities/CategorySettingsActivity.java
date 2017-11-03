package com.android.budget.activities;

import android.content.Context;
import android.content.Intent;
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

        Toolbar toolbar = findViewById(R.id.toolbar_category);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        etCategoryName = findViewById(R.id.etCategoryName);
        fab = findViewById(R.id.btnSelectedCategoryImage);
        fab.setImageResource(R.drawable.coin);
        fab.setTag(fab.getId(), R.drawable.coin);

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorySettingsActivity.this.finish();
            }
        });

        initializeData(selectedCategoryId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCategoryChooser();
            }
        });

    }

    /**
     * Open the category image selection activity
     */
    public void openCategoryChooser() {
        Intent intent = new Intent(this, CategoryChooseActivity.class);
        startActivityForResult(intent, 1);
    }

    public void initializeData(Integer selectedCategoryId){
        if(selectedCategoryId == null){
            category = new Category();
            category.setSrc_image((Integer) fab.getTag(fab.getId()));
        } else {
            category = categoryDAO.findCategoryById(selectedCategoryId);
            etCategoryName.setText(category.getName_category());
            fab.setImageResource(category.getSrc_image());
            fab.setTag(fab.getId(), category.getSrc_image());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Integer image_src = data.getIntExtra("selectedImage", -1);
        fab.setImageResource(image_src);
        fab.setTag(fab.getId(), image_src);
        category.setSrc_image(image_src);
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
                        Integer selectedAccountId = MainActivity.preferences.getInt("selectedAccount", -1);
                        if(selectedAccountId != -1) category.setId_account(selectedAccountId);
                        category.setName_category(etCategoryName.getText().toString());
                        categoryDAO.add(category);
                    } else {
                        categoryDAO.updateById(category);
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
