package com.example.tamp.data.adapeter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(ToDo todo);
    }

    public interface OnItemClickListener {
        void onItemClick(ToDo todo);
    }

    public ToDoAdapter(List<ToDo> toDoList) {
        this.toDoList = toDoList;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void updateData(List<ToDo> newToDoList) {
        toDoList = newToDoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo toDo = toDoList.get(position);
        holder.bindData(toDo, onItemClickListener, onItemLongClickListener);
        if(toDo.getStatus()) {
            holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconmonstr_check_mark_circle_lined, 0, 0, 0);
        } else {
            holder.tvContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconmonstr_check_mark_14, 0, 0, 0);
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

        public void bindData(ToDo todo,
                             OnItemClickListener clickListener,
                             OnItemLongClickListener longClickListener) {
            tvContent.setText(todo.getListContent());

            if(todo.getStatus()) {
                int[] colors = {
                        R.color.soft_green,
                        R.color.soft_blue,
                        R.color.soft_pink,
                        R.color.soft_yellow
                };
                Random rand = new Random();
                int randomColor = colors[rand.nextInt(colors.length)];
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), randomColor));
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.completed_gray));
            }

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(todo);
                    return true;
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(todo);
                }
            });
        }
    }
}

