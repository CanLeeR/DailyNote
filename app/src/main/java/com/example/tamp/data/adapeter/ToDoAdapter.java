package com.example.tamp.data.adapeter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.entities.ToDo;

import java.util.List;
import java.util.Random;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList;


    @Override
    public int getItemCount() {
        return toDoList.size();
    }
    public ToDoAdapter(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);

        return new ToDoViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);
        holder.tvContent.setText(toDo.getListContent());

        if(toDo.getStatus()) { // 如果清单项为未完成状态
            int[] colors = {
                    R.color.soft_green,
                    R.color.soft_blue,
                    R.color.soft_pink,
                    R.color.soft_yellow
            };
            Random rand = new Random();
            int randomColor = colors[rand.nextInt(colors.length)];
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), randomColor));
        } else { // 如果清单项为已完成状态
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.completed_gray));
        }
    }



    public static class ToDoViewHolder extends RecyclerView.ViewHolder {

        TextView tvContent;
        CardView cardView;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


}
