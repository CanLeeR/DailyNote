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
    private AppDatabase db;
    private DailyDao dailyDao;
    private RecyclerView diaryRecyclerView;
    private DiaryAdapter diaryAdapter;
    private List<Daily> diaries;

    int userId;

    private View view;
    final View finalView = null;  // 创建一个final的引用

    UserUtils userUtils;

    private DiaryViewModel diaryViewModel;



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
        this.view = view;
        diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView);


        // 设置ActionBar
        setActionBar();
        getDaily(view);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddDiaryActivity.class);
            startActivity(intent);
        });
        AppDatabase db = AppDatabase.getInstance(getContext());
        DailyDao dailyDao = db.dailyDao();
        UserRepository userRepository = new UserRepository(getContext());
        DiaryViewModel diaryViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new DiaryViewModel(dailyDao, userRepository);
            }
        }).get(DiaryViewModel.class);


        diaryViewModel.getDiaries().observe(getViewLifecycleOwner(), diaries -> {
            Log.d("DiaryFragment", "1111111");
            // 在这里，当LiveData中的数据发生变化时，这个方法会被调用。
            // 更新UI，例如刷新RecyclerView的数据
            if(diaryAdapter == null) {
                diaryAdapter = new DiaryAdapter(diaries);
                diaryRecyclerView.setAdapter(diaryAdapter);
            } else {
                diaryAdapter.updateData(diaries);
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("DiaryFragment", "Fragment is in onResume, active");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DiaryFragment", "Fragment is in onPause");
    }

//...同样，也可以为其他生命周期方法



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.view = null;
    }


    private void getDaily(final View view) {
        Executors.newSingleThreadExecutor().execute(() -> {
//            dailyDao.insertDaily(new Daily(1,"Title 2", "Content  1 ...",LocalDate.now()));
//            dailyDao.insertDaily(new Daily(1,"Title 3", "Content  1 ...",LocalDate.now()));
//            dailyDao.insertDaily(new Daily(1,"Title 4", "Content  1 ...",LocalDate.now()));

                    userId = userUtils.getLoggedInUserId(getContext());
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
                List<Daily> filteredDailies = diaries.stream()
                        .filter(daily -> daily.getTitle().toLowerCase().contains(query.toLowerCase())
                                || daily.getContent().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());

                // 更新RecyclerView的适配器
                DiaryAdapter diaryAdapter = new DiaryAdapter(filteredDailies);
                RecyclerView diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView);
                diaryRecyclerView.setAdapter(diaryAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    DiaryAdapter diaryAdapter = new DiaryAdapter(diaries);
                    RecyclerView diaryRecyclerView = view.findViewById(R.id.diaryRecyclerView);
                    diaryRecyclerView.setAdapter(diaryAdapter);
                }
                return false;
            }
        });

    }
}
