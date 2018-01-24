package com.android.budget.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.budget.R;
import com.android.budget.fragments.FragmentAccount;
import com.android.budget.fragments.FragmentBalance;
import com.android.budget.fragments.FragmentOther;
import com.android.budget.fragments.FragmentStatistic;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences preferences;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);

        navigationView = findViewById(R.id.bottom_navigation);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.account:
                        showFragment(new FragmentAccount());
                        break;
                    case R.id.balance:
                        showFragment(new FragmentBalance());
                        break;
                    case R.id.statistic:
                        showFragment(new FragmentStatistic());
                        break;
                    case R.id.other:
                        showFragment(new FragmentOther());
                        break;
                }

                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();

        showAccountFragment();

        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    public void showAccountFragment() {
        navigationView.setSelectedItemId(R.id.account);
    }

    public void showBalanceFragment() {
        navigationView.setSelectedItemId(R.id.balance);
    }

    public void showStatisticFragment() {
        navigationView.setSelectedItemId(R.id.statistic);
    }

    public void showOtherFragment() {
        navigationView.setSelectedItemId(R.id.other);
    }
}
