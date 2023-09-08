package com.example.tamp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.utils.UserUtils;
import com.example.tamp.viewModel.AddDiaryViewModel;

import java.time.LocalDate;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private AddDiaryViewModel viewModel;
    UserUtils userUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);

        // 初始化ViewModel
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        DailyDao dailyDao = db.dailyDao();
        viewModel = new AddDiaryViewModel(dailyDao);
    }

    public void saveDiary(View view) {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        int dailyId = userUtils.getLoggedInUserId(this);
        LocalDate date = LocalDate.now();
        viewModel.saveDiary(dailyId,title, content,date);

        finish();
    }
}
