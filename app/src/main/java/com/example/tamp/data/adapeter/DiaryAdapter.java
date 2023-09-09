package com.example.tamp.data.adapeter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.entities.Daily;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Daily> diaryList;
    private OnDiaryDeleteListener deleteListener;  // 删除监听器

    public DiaryAdapter(List<Daily> diaryList) {
        this.diaryList = diaryList;
    }

    // 设置删除监听器的方法
    public void setOnDiaryDeleteListener(OnDiaryDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        Daily currentDiary = diaryList.get(position);
        holder.tvTitle.setText(currentDiary.getTitle());
        holder.tvContentPreview.setText(currentDiary.getContent());
        holder.tvDate.setText(currentDiary.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public void updateData(List<Daily> newDiaries) {
        diaryList = newDiaries;
        notifyDataSetChanged();
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContentPreview, tvDate;
        ImageButton deleteButton;


        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContentPreview = itemView.findViewById(R.id.tvContentPreview);
            tvDate = itemView.findViewById(R.id.tvDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (deleteListener != null && position != RecyclerView.NO_POSITION) {
                    Daily diaryToDelete = diaryList.get(position);
                    deleteListener.onDelete(diaryToDelete); // 触发监听器
                    diaryList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }

    public interface OnDiaryDeleteListener {
        void onDelete(Daily diary);
    }
}
