package com.android.budget.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("mySettings", Context.MODE_PRIVATE);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelected(true);
        navigationView.setSelectedItemId(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentAccount()).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.account:
                        FragmentAccount fragmentAccount = new FragmentAccount();
                        fragmentTransaction.replace(R.id.fragment, fragmentAccount);
                        break;
                    case R.id.balance:
                        FragmentBalance fragmentBalance = new FragmentBalance();
                        fragmentTransaction.replace(R.id.fragment, fragmentBalance);
                        break;
                    case R.id.statistic:
                        FragmentStatistic fragmentStatistic = new FragmentStatistic();
                        fragmentTransaction.replace(R.id.fragment, fragmentStatistic);
                        break;
                    case R.id.other:
                        FragmentOther fragmentOther = new FragmentOther();
                        fragmentTransaction.replace(R.id.fragment, fragmentOther);
                        break;
                }

                fragmentTransaction.commit();

                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
    }
}
