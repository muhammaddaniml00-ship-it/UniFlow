package com.student.uniflow.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.student.uniflow.R;
import com.student.uniflow.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private List<Task> taskList;
    private final OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onToggleComplete(Task task, boolean checked);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskActionListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvSubject.setText(task.getSubject() != null && !task.getSubject().isEmpty()
                ? task.getSubject() : "No Subject");
        holder.tvDeadline.setText(task.getDeadline() != null && !task.getDeadline().isEmpty()
                ? "📅 " + task.getDeadline() : "No deadline");

        // Prevent checkbox listener from firing during bind
        holder.cbCompleted.setOnCheckedChangeListener(null);
        holder.cbCompleted.setChecked(task.isCompleted());

        // Strike-through if completed
        if (task.isCompleted()) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setAlpha(0.5f);
            holder.tvSubject.setAlpha(0.5f);
            holder.tvDeadline.setAlpha(0.5f);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setAlpha(1f);
            holder.tvSubject.setAlpha(1f);
            holder.tvDeadline.setAlpha(1f);
        }

        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) listener.onToggleComplete(task, isChecked);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(task);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvSubject, tvDeadline;
        CheckBox cbCompleted;
        ImageButton btnEdit, btnDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardTask);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvSubject = itemView.findViewById(R.id.tvTaskSubject);
            tvDeadline = itemView.findViewById(R.id.tvTaskDeadline);
            cbCompleted = itemView.findViewById(R.id.cbTaskCompleted);
            btnEdit = itemView.findViewById(R.id.btnEditTask);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }
    }
}
