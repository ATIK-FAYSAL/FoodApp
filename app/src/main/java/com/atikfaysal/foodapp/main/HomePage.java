package com.atikfaysal.foodapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.atikfaysal.foodapp.R;
import com.atikfaysal.foodapp.backend.SharedPreferencesData;
import com.atikfaysal.foodapp.fragments.History;
import com.atikfaysal.foodapp.fragments.Home;
import com.atikfaysal.foodapp.fragments.More;
import com.atikfaysal.foodapp.fragments.Profile;
import com.atikfaysal.foodapp.interfaces.InitialMethods;

import java.util.List;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements InitialMethods
{

    private SharedPreferencesData sharedPreferencesData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initComponent();
        setToolbar();
    }


    @Override
    public void initComponent() {
        sharedPreferencesData = new SharedPreferencesData(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);//set navigation listener
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Home()).commit();
    }

    @Override
    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.mHome:
                    fragment = new Home();
                    break;

                case R.id.mHistory:
                    fragment = new History();
                    break;

                case R.id.mProfile:
                    fragment = new Profile();
                    break;

                case R.id.mMore:
                    fragment = new More();
                    break;
            }

            assert fragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();

            return true;
        }
    };

    @Override
    public void processJsonData(String json) {

    }

    @Override
    public List<?> processJsonValue(String json) {
        return null;
    }


}
