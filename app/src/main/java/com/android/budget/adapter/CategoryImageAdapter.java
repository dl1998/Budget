package com.android.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.budget.R;
import com.android.budget.adapter.model.CategoryImageModel;

/**
 * Created by dl1998 on 30.10.17.
 */

public class CategoryImageAdapter extends BaseAdapter {
    private final Context context;
    private final CategoryImageModel[] models;
    private final LayoutInflater inflater;

    public CategoryImageAdapter(Context context, CategoryImageModel[] models) {
        this.context = context;
        this.models = models;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.length;
    }

    @Override
    public Object getItem(int position) {
        return models[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.category_image_row, parent, false);
        }

        CategoryImageModel listModel = models[position];

        ImageView circleButton = view.findViewById(R.id.cvButton);
        circleButton.setImageResource(listModel.getImage_src());
        circleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.on_touch_button_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.on_touch_button_released));
                        break;
                }
                return false;
            }
        });

        return view;
    }
}
