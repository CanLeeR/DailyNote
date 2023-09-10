package com.example.tamp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tamp.R;
import com.example.tamp.fragments.DiaryFragment;
import com.example.tamp.fragments.ListsFragment;
import com.example.tamp.fragments.MyFragment;
import com.example.tamp.utils.UserUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Fragment diaryFragment;
    private Fragment listsFragment;
    private Fragment myFragment;
    UserUtils userUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            initializeFragments();
        } else {
            restoreFragments();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

    }

    private void initializeFragments() {
        diaryFragment = new DiaryFragment();
        listsFragment = new ListsFragment();
        myFragment = new MyFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, diaryFragment, "TAG_DIARY_FRAGMENT");
        transaction.add(R.id.fragment_container, listsFragment, "TAG_LISTS_FRAGMENT");
        transaction.add(R.id.fragment_container, myFragment, "TAG_MY_FRAGMENT");
        transaction.hide(listsFragment).hide(myFragment);
        transaction.commit();
    }

    private void restoreFragments() {
        diaryFragment = getSupportFragmentManager().findFragmentByTag("TAG_DIARY_FRAGMENT");
        listsFragment = getSupportFragmentManager().findFragmentByTag("TAG_LISTS_FRAGMENT");
        myFragment = getSupportFragmentManager().findFragmentByTag("TAG_MY_FRAGMENT");
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Fragment selectedFragment = null;
        String title = "";
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.daily_icon:
                transaction.hide(listsFragment).hide(myFragment).show(diaryFragment);
                title = "Inspiration";
                break;
            case R.id.list_icon:
                transaction.hide(diaryFragment).hide(myFragment).show(listsFragment);
                title = "ToDo";
                break;
            case R.id.my_icon:
                transaction.hide(diaryFragment).hide(listsFragment).show(myFragment);
                title = "Creation";
                break;
            default:
                return false;
        }

        transaction.commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        userUtils.clearLoggedInUserId(this);
        Log.d("flag","onDestroy() called");
        super.onDestroy();
    }
}