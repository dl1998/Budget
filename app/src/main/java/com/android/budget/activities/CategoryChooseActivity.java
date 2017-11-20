package com.android.budget.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.budget.R;
import com.android.budget.adapter.CategoryImageAdapter;

import java.util.ArrayList;

/**
 * Created by dl1998 on 29.10.17.
 */

public class CategoryChooseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_category_choose);

        Toolbar toolbar = findViewById(R.id.toolbar_category_choose);
        setSupportActionBar(toolbar);

        GridView gvImages = findViewById(R.id.gvCategories);

        TypedArray imgs = getResources().obtainTypedArray(R.array.drawables);

        final ArrayList<Integer> listModels = new ArrayList<>();
        for (int i = 0; i < imgs.length(); i++) {
            listModels.add(imgs.getResourceId(i, -1));
        }

        gvImages.setAdapter(new CategoryImageAdapter(this, listModels.toArray(new Integer[listModels.size()])));

        gvImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("selectedImage", listModels.get(i));
                setResult(RESULT_OK, intent);
                CategoryChooseActivity.this.finish();
            }
        });

        toolbar.setNavigationIcon(R.mipmap.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryChooseActivity.this.finish();
            }
        });
    }
}
