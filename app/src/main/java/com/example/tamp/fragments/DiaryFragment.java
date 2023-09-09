package com.example.tamp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.adapeter.DiaryAdapter;
import com.example.tamp.data.entities.Daily;
import com.example.tamp.data.repository.UserRepository;
import com.example.tamp.ui.activities.AddDiaryActivity;
import com.example.tamp.utils.UserUtils;
import com.example.tamp.viewModel.DiaryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DiaryFragment extends Fragment {

    private RecyclerView diaryRecyclerView;
    private DiaryAdapter diaryAdapter;
    private DailyDao dailyDao;
    List<Daily> diaries;
    Button deleteButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase db = AppDatabase.getInstance(getContext());
        dailyDao = db.dailyDao();
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

        setActionBar();


        diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView);
        diaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddDiaryActivity.class);
            startActivity(intent);
        });

        fetchAndDisplayDiaries();
    }


    private void fetchAndDisplayDiaries() {
        Executors.newSingleThreadExecutor().execute(() -> {
            diaries = dailyDao.getByUserId(1);

            getActivity().runOnUiThread(() -> {
                if (diaryAdapter == null) {
                    diaryAdapter = new DiaryAdapter(diaries);
                    diaryRecyclerView.setAdapter(diaryAdapter);
                    diaryAdapter.setOnDiaryDeleteListener(this::deleteDiary);  // 移动到这里
                } else {
                    diaryAdapter.updateData(diaries);
                }
            });
        });
    }

    private void deleteDiary(Daily diary) {
        Executors.newSingleThreadExecutor().execute(() -> {
            dailyDao.deleteDaily(diary);
            fetchAndDisplayDiaries();  // 更新UI
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchAndDisplayDiaries();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("DiaryFragment", "Fragment is in onPause");
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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAndDisplayDiaries(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    diaryAdapter.updateData(diaries);
                } else {
                    filterAndDisplayDiaries(newText);
                }
                return false;
            }
        });
    }

    private void filterAndDisplayDiaries(String query) {
        if (diaries != null) {
            List<Daily> filteredDailies = diaries.stream()
                    .filter(daily -> daily.getTitle().toLowerCase().contains(query.toLowerCase())
                            || daily.getContent().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            diaryAdapter.updateData(filteredDailies);
        }
    }

}
