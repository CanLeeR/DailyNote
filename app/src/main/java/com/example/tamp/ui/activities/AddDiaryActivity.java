package com.example.tamp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.repository.UserRepository;
import com.example.tamp.utils.UserUtils;
import com.example.tamp.viewModel.AddDiaryViewModel;
import com.example.tamp.viewModel.DiaryViewModel;

import java.time.LocalDate;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private AddDiaryViewModel viewModel;
    private UserUtils userUtils;
    private DiaryViewModel diaryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);

        userUtils = new UserUtils();  // 如果UserUtils有参数的构造函数，请确保传递所需的参数

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                AppDatabase db = AppDatabase.getInstance(AddDiaryActivity.this);
                if (modelClass.isAssignableFrom(AddDiaryViewModel.class)) {
                    return (T) new AddDiaryViewModel(db.dailyDao());
                } else if (modelClass.isAssignableFrom(DiaryViewModel.class)) {
                    UserRepository userRepository = new UserRepository(AddDiaryActivity.this);
                    return (T) new DiaryViewModel(db.dailyDao(), userRepository);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        };

        viewModel = new ViewModelProvider(this, factory).get(AddDiaryViewModel.class);
        diaryViewModel = new ViewModelProvider(this, factory).get(DiaryViewModel.class);
    }

    public void saveDiary(View view) {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        int dailyId = userUtils.getLoggedInUserId(this);
        LocalDate date = LocalDate.now();
        viewModel.saveDiary(dailyId, title, content, date);

        diaryViewModel.refreshDiaries(); // 刷新LiveData

        finish();
    }
}
