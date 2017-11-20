package com.android.budget.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.budget.R;

/**
 * Created by dimal on 08.10.2017.
 */

public class FragmentOther extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_other, container, false);

        Toolbar toolbar;
        if (container != null) {
            toolbar = container.getRootView().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.other);
        }

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

        return view;
    }
}
