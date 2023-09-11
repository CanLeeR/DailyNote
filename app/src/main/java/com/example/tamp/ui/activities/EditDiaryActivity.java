package com.example.tamp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.entities.Daily;

import java.time.LocalDate;
import java.util.concurrent.Executors;

public class EditDiaryActivity extends AppCompatActivity {
    EditText  titleEditText;
    EditText  contentEditText;

    DailyDao dailyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        AppDatabase db = AppDatabase.getInstance(this);
        dailyDao = db.dailyDao();

        int diaryId = getIntent().getIntExtra("DIARY_ID", -1);
        Log.d("flag", "onCreate: diaryId = "+ diaryId);
        displayTitleAndContent(diaryId);
        Button saveButton = findViewById(R.id.submitUpdate);
        saveButton.setOnClickListener(v -> saveEdits(diaryId));

    }

    private void displayTitleAndContent(int diaryId) {
        if (diaryId != -1) {
            Executors.newSingleThreadExecutor().execute(() -> {
                Daily diary = dailyDao.getDiary(diaryId);
                if (diary != null) {
                    titleEditText.setText(diary.getTitle());
                    contentEditText.setText(diary.getContent());
                }
            });
        }
    }
    private void saveEdits(int diaryId) {
        String editedTitle = titleEditText.getText().toString();
        String editedContent = contentEditText.getText().toString();
        Executors.newSingleThreadExecutor().execute(() -> {
            Daily diary = dailyDao.getDiary(diaryId);
            if (diary != null) {
                // 更新日记实体的内容
                diary.setTitle(editedTitle);
                diary.setContent(editedContent);
                diary.setDate(LocalDate.now());
                // 将日记实体的变更保存回数据库
                dailyDao.updateDiary(diary);
            }
        });
        finish();
    }

}