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

    // 使用 MutableLiveData，因为我们想在ViewModel内部修改它
    private final MutableLiveData<List<Daily>> diariesLiveData = new MutableLiveData<>();

    private final DailyDao dailyDao;

    public DiaryViewModel(DailyDao dailyDao, UserRepository userRepository) {
        this.dailyDao = dailyDao;
    }

    // 对外暴露为基类LiveData，这样外部类不能修改它，只能观察它
    public LiveData<List<Daily>> getDiaries() {
        return diariesLiveData;
    }

    // 更新LiveData的方法
    public void fetchDiaries() {
        // 这是一个示例，假设我们在后台线程中从数据库获取数据
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Daily> diaries = dailyDao.getByUserId(1);
            diariesLiveData.postValue(diaries); // 使用 postValue 更新LiveData
        });
    }

    public void saveDiary(String title, String content) {
        Executors.newSingleThreadExecutor().execute(() -> {

            LocalDate date = LocalDate.now();
            Daily newDiary = new Daily(1, title, content, date);
            dailyDao.insertDaily(newDiary);
            fetchDiaries(); // 数据库更新后，重新获取日记列表
        });
    }

}



