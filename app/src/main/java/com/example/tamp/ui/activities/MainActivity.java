package com.example.tamp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.tamp.fragments.DiaryFragment;
import com.example.tamp.fragments.ListsFragment;
import com.example.tamp.fragments.MyFragment;
import com.example.tamp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.daily_icon:
                    selectedFragment = new DiaryFragment();
                    break;
                case R.id.list_icon:
                    selectedFragment = new ListsFragment();
                    break;
                case R.id.my_icon:
                    selectedFragment = new MyFragment();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });

        // 默认显示 DiaryFragment
        if (savedInstanceState == null) {  // 为了避免在设备旋转时重复加载
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DiaryFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.daily_icon);  // 设置默认选中的底部导航项
        }
    }


}