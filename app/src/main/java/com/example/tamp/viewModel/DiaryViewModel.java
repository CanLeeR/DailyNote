package com.example.tamp.viewModel;

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

    private final MutableLiveData<List<Daily>> diariesLiveData = new MutableLiveData<>();

    private final DailyDao dailyDao;

    UserRepository userRepository;


    public DiaryViewModel(DailyDao dailyDao, UserRepository userRepository) {
        this.dailyDao = dailyDao;
        this.userRepository = userRepository;  // 初始化 userRepository
    }


    // 更新LiveData的方法
    public void fetchDiaries() {

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Daily> diaries = dailyDao.getByUserId(userRepository.getLoggedInUserId());
            diariesLiveData.postValue(diaries); // 使用 postValue 更新LiveData
        });
    }

    public void saveDiary(String title, String content) {
        Executors.newSingleThreadExecutor().execute(() -> {

            LocalDate date = LocalDate.now();
            Daily newDiary = new Daily(userRepository.getLoggedInUserId(), title, content, date);
            dailyDao.insertDaily(newDiary);
            fetchDiaries(); // 数据库更新后，重新获取日记列表
        });
    }

}



