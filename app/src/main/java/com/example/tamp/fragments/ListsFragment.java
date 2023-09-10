package com.example.tamp.fragments;

import androidx.fragment.app.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.ToDoDao;
import com.example.tamp.data.adapeter.ToDoAdapter;
import com.example.tamp.data.entities.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class ListsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDo> toDoList = new ArrayList<>();
    private ToDoDao toDoDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase db = AppDatabase.getInstance(getContext());
        toDoDao = db.toDoDao();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadSampleData();
        Log.d("ListsFragment", "Size of toDoList: " + toDoList.size());
        toDoAdapter = new ToDoAdapter(toDoList);
        recyclerView.setAdapter(toDoAdapter);


        FloatingActionButton fabAddToDo = view.findViewById(R.id.fab_add_todo);
        fabAddToDo.setOnClickListener(v -> showAddToDoDialog());
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // 不处理上下移动的事件
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) { // 如果是向右滑动
                    int position = viewHolder.getAdapterPosition(); // 获取当前项的位置
                    ToDo toDo = toDoList.get(position);
                    toDo.setStatus(false); // 修改状态为已完成
                    toDoDao.updateToDo(toDo); // 更新数据库
                    toDoAdapter.notifyDataSetChanged(); // 刷新适配器
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


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
            newTodo.setListContent(todoContent);

            Executors.newSingleThreadExecutor().execute(() -> {
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
        for (int i = 0; i < 10; i++) {
            ToDo toDo = new ToDo();
            toDo.setListContent("ToDo Item " + (i+1));
            toDo.setStatus(true);
            toDoList.add(toDo);
        }
        recyclerView.setAdapter(toDoAdapter);
    }

}
