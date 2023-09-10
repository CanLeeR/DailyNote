package com.example.tamp.fragments;

import androidx.fragment.app.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.ToDoDao;
import com.example.tamp.data.adapeter.ToDoAdapter;
import com.example.tamp.data.entities.ToDo;
import com.example.tamp.data.repository.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDo> toDoList = new ArrayList<>();
    private ToDoDao toDoDao;
    UserRepository userRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase db = AppDatabase.getInstance(getContext());
        toDoDao = db.toDoDao();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userRepository = new UserRepository(context); // 初始化 UserRepository
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadSampleData();
        toDoAdapter = new ToDoAdapter(toDoList);
        recyclerView.setAdapter(toDoAdapter);


        FloatingActionButton fabAddToDo = view.findViewById(R.id.fab_add_todo);
        fabAddToDo.setOnClickListener(v -> showAddToDoDialog());
        swipeToChangeStatus();


        return view;
    }

    private void showAddToDoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加清单");

        // 设置一个EditText控件来输入清单内容
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("添加", (dialog, which) -> {
            String todoContent = input.getText().toString();

            // 创建一个新的ToDo对象
            ToDo newTodo = new ToDo();
            newTodo.setStatus(true);

            newTodo.setListContent(todoContent);

            Executors.newSingleThreadExecutor().execute(() -> {
                newTodo.setUserId(userRepository.getLoggedInUserId());
                // 插入到数据库
                toDoDao.insertToDo(newTodo);

                // 更新适配器
                getActivity().runOnUiThread(() -> {
                    toDoList.add(newTodo);
                    toDoAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "清单已添加!", Toast.LENGTH_SHORT).show();
                });
            });
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }




    private void loadSampleData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            int userId = userRepository.getLoggedInUserId();
            toDoList = toDoDao.getToDoByUserId(userId);

            getActivity().runOnUiThread(() -> {
                if (toDoAdapter == null) {
                    toDoAdapter = new ToDoAdapter(toDoList);

                    recyclerView.setAdapter(toDoAdapter);
                } else {
                    toDoAdapter.updateData(toDoList);
                }
            });
        });
    }
    private void swipeToChangeStatus() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                ToDo task = toDoList.get(position);

                // 根据滑动方向切换任务状态
                if (swipeDir == ItemTouchHelper.LEFT) {
                    task.setStatus(true); // 未完成
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    task.setStatus(false); // 已完成
                }

                // 更新数据库
                Executors.newSingleThreadExecutor().execute(() -> {
                    toDoDao.updateToDo(task);

                    // 重新从数据库加载数据并刷新UI
                    toDoList = toDoDao.getToDoByUserId(userRepository.getLoggedInUserId());

                    getActivity().runOnUiThread(() -> {
                        toDoAdapter.updateData(toDoList); // 确保你有一个这样的方法来更新整个数据集
                    });
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
