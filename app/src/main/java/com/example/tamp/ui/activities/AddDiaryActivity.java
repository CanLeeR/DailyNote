package com.example.tamp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.repository.UserRepository;
import com.example.tamp.viewModel.DiaryViewModel;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private DiaryViewModel diaryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                AppDatabase db = AppDatabase.getInstance(AddDiaryActivity.this);
                UserRepository userRepository = new UserRepository(AddDiaryActivity.this);

                if (modelClass.isAssignableFrom(DiaryViewModel.class)) {
                    return (T) new DiaryViewModel(db.dailyDao(), userRepository);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        };

        diaryViewModel = new ViewModelProvider(this, factory).get(DiaryViewModel.class);
    }

    public void saveDiary(View view) {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        diaryViewModel.saveDiary(title, content); // 使用 DiaryViewModel 的方法保存日记
        finish();
    }
}