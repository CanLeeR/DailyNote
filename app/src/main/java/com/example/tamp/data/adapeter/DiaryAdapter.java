package com.example.tamp.data.adapeter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamp.R;
import com.example.tamp.data.entities.Daily;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Daily> diaryList;

    private OnDiaryClickListener listener;

    public DiaryAdapter(List<Daily> diaryList) {
        this.diaryList = diaryList;
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

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContentPreview = itemView.findViewById(R.id.tvContentPreview);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemView.setOnLongClickListener(v -> {
                if (onDiaryLongClickListener != null) {
                    onDiaryLongClickListener.onLongClick(diaryList.get(getAdapterPosition()));
                    return true;
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDiaryClick(diaryList.get(position));
                }
            });

        }
    }
    private OnDiaryLongClickListener onDiaryLongClickListener;

    public void setOnDiaryLongClickListener(OnDiaryLongClickListener listener) {
        this.onDiaryLongClickListener = listener;
    }

    public interface OnDiaryLongClickListener {
        void onLongClick(Daily diary);
    }


    public interface OnDiaryClickListener {
        void onDiaryClick(Daily diary);
    }

    public void setOnDiaryClickListener(OnDiaryClickListener listener) {
        this.listener = listener;
    }
}
