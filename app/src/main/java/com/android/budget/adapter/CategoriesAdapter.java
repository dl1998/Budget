package com.android.budget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.budget.R;
import com.android.budget.entity.Category;

/**
 * Created by dimal on 24.10.2017.
 */

public class CategoriesAdapter extends BaseAdapter {
    private final Context context;
    private final Category[] categories;
    private final LayoutInflater inflater;

    public CategoriesAdapter(Context context, Category[] categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.categories_button_preset, parent, false);
        }

        Category listModel = categories[position];

        ImageView circleButton = (ImageView) view.findViewById(R.id.circleView);
        circleButton.setImageResource(listModel.getSrc_image());
        circleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
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
        ((TextView) view.findViewById(R.id.circleViewText)).setText(listModel.getName_category());

        return view;
    }
}
