package com.example.tamp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tamp.data.Dao.DailyDao;
import com.example.tamp.data.entities.Daily;
import com.example.tamp.data.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class DiaryViewModel extends ViewModel {
    private final DailyDao dailyDao;
    private final UserRepository userRepository;

    public DiaryViewModel(DailyDao dailyDao, UserRepository userRepository) {
        this.dailyDao = dailyDao;
        this.userRepository = userRepository;
        refreshDiaries();
    }
    private MutableLiveData<List<Daily>> diariesLiveData = new MutableLiveData<>();

    public LiveData<List<Daily>> getDiaries() {
        return diariesLiveData;
    }

    public void refreshDiaries() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Daily> updatedDiaries = dailyDao.getByUserId(userRepository.getLoggedInUserId());

            Log.d("DiaryViewModel", "Fetched " + " diaries from database.");

            diariesLiveData.postValue(updatedDiaries);
        });
    }

    // 2. 添加新日记
    public void addDiary(String title, String content) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Daily daily = new Daily(userRepository.getLoggedInUserId(), title, content, LocalDate.now());
            dailyDao.insertDaily(daily);
            refreshDiaries();
        });
    }

    // 3. 更新日记
    public void updateDiary(Daily daily) {
        Executors.newSingleThreadExecutor().execute(() -> {
            dailyDao.updateDaily(daily);
            refreshDiaries();
        });
    }

    // 4. 删除日记
    public void deleteDiary(Daily daily) {
        Executors.newSingleThreadExecutor().execute(() -> {
            dailyDao.deleteDaily(daily);
            refreshDiaries();
        });
    }
}
