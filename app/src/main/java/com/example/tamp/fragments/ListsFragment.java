package com.example.tamp.fragments;

import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.adapeter.ToDoAdapter;
import com.example.tamp.data.entities.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDo> toDoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        toDoAdapter = new ToDoAdapter(toDoList);
        recyclerView.setAdapter(toDoAdapter);

        return view;
    }

    private void loadSampleData() {
        for (int i = 0; i < 20; i++) {
            ToDo toDo = new ToDo();
            toDo.setListContent("ToDo Item " + (i+1));
            toDoList.add(toDo);
        }
    }

}
