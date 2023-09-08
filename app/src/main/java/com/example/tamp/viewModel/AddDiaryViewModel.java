package com.example.tamp.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.entities.Daily;
import com.example.tamp.utils.UserUtils;

import java.time.LocalDate;
import java.util.concurrent.Executors;

public class AddDiaryViewModel extends ViewModel {

    private final DailyDao dailyDao;

    public AddDiaryViewModel(DailyDao dailyDao) {
        this.dailyDao = dailyDao;
    }

    public void saveDiary(int dailyId, String title, String content, LocalDate date) {
        Executors.newSingleThreadExecutor().execute(() -> {

            Daily newDiary = new Daily(dailyId,title, content, date);

            dailyDao.insertDaily(newDiary);
        });
    }

}
