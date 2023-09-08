package com.example.tamp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.adapeter.DiaryAdapter;
import com.example.tamp.data.entities.Daily;


import java.util.List;
import java.util.concurrent.Executors;

public class DiaryFragment extends Fragment {
    private AppDatabase db;
    private DailyDao dailyDao;

    private View view;
    final View finalView = null;  // 创建一个final的引用


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getInstance(getContext());
        dailyDao = db.dailyDao();
    }

    public DiaryFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置ActionBar
        setActionBar();
        getDaily(view);
    }


    private void getDaily(final View view) {
        Executors.newSingleThreadExecutor().execute(() -> {
//            dailyDao.insertDaily(new Daily(1,"Title 2", "Content  1 ...",LocalDate.now()));
//            dailyDao.insertDaily(new Daily(1,"Title 3", "Content  1 ...",LocalDate.now()));
//            dailyDao.insertDaily(new Daily(1,"Title 4", "Content  1 ...",LocalDate.now()));

                    int userId = getLoggedInUserId();
                    List<Daily> diaries;
                    if (userId != -1) {
                        diaries = dailyDao.getByUserId(userId);
                        if (isAdded()) {  // 检查有没有添加到
                            getActivity().runOnUiThread(() -> {
                                RecyclerView diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView);
                                DiaryAdapter diaryAdapter = new DiaryAdapter(diaries);
                                diaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                diaryRecyclerView.setAdapter(diaryAdapter);
                                //添加分割线
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(diaryRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                                diaryRecyclerView.addItemDecoration(dividerItemDecoration);
                            });
                        }
                    } else {
                        throw new RuntimeException("数据异常");
                    }
                }
        );
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }


    private void setActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Daily");//title
                actionBar.setDisplayHomeAsUpEnabled(false);//返回键
                setHasOptionsMenu(true);//选项菜单
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.diary_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // 处理菜单项的点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_diary:
                // 执行搜索操作
                return true;
            case R.id.add_diary:
                // 跳转到添加日记的界面或弹出Dialog
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
