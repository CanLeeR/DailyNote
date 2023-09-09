package com.example.tamp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tamp.R;
import com.example.tamp.data.repository.DiaryRepository;
import com.example.tamp.data.repository.ListRepository;
import com.example.tamp.data.repository.UserRepository;
import com.example.tamp.ui.activities.LoginActivity;

public class MyFragment extends Fragment {

    private TextView userNameTextView;
    private TextView diaryCountTextView;
    private TextView listCountTextView;
    private UserRepository userRepository;
    private DiaryRepository diaryRepository;
    private ListRepository listRepository;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  // 这样才能让Fragment添加自己的选项菜单。

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userRepository = new UserRepository(context);
        diaryRepository = new DiaryRepository(context);
        listRepository = new ListRepository(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userNameTextView = view.findViewById(R.id.userName);
        diaryCountTextView = view.findViewById(R.id.diaryCount);
        listCountTextView = view.findViewById(R.id.listCount);

        fetchAndDisplayData();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_option) {

            //清除信息
            SharedPreferences preferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();


            // 跳转到登录界面并清除其他活动
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchAndDisplayData() {
        // 获取用户名
        String username = userRepository.getUserName();
        userNameTextView.setText(username);

//        // 获取日记数量
//        int diaryCount = diaryRepository.getDiaryCount();
//        diaryCountTextView.setText (diaryCount);
//
//        // 获取清单数量
//        int listCount = listRepository.getListCount();
//        listCountTextView.setText(listCount);
    }


}
