package com.example.tamp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.Dao.UserDao;
import com.example.tamp.fragments.DiaryFragment;
import com.example.tamp.fragments.ListsFragment;
import com.example.tamp.fragments.MyFragment;
import com.example.tamp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentA;
    private Fragment fragmentB;
    private Fragment fragmentC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (savedInstanceState == null) {  // 如果是首次加载
            fragmentA = new DiaryFragment();
            fragmentB = new ListsFragment();
            fragmentC = new MyFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, fragmentA, "TAG_FRAGMENT_A");
            transaction.add(R.id.fragment_container, fragmentB, "TAG_FRAGMENT_B");
            transaction.add(R.id.fragment_container, fragmentC, "TAG_FRAGMENT_C");
            transaction.hide(fragmentA).hide(fragmentB).hide(fragmentC);  // 默认隐藏它们
            transaction.show(fragmentA);  // 默认显示第一个Fragment
            transaction.commit();
        } else {
            fragmentA = getSupportFragmentManager().findFragmentByTag("TAG_FRAGMENT_A");
            fragmentB = getSupportFragmentManager().findFragmentByTag("TAG_FRAGMENT_B");
            fragmentC = getSupportFragmentManager().findFragmentByTag("TAG_FRAGMENT_C");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.daily_icon:
                    transaction.hide(fragmentB).hide(fragmentC).show(fragmentA);
                    break;
                case R.id.list_icon:
                    transaction.hide(fragmentA).hide(fragmentC).show(fragmentB);
                    break;
                case R.id.my_icon:
                    transaction.hide(fragmentA).hide(fragmentB).show(fragmentC);
                    break;
            }
            transaction.commit();

            return true;
        });


    }


}