package com.student.uniflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.student.uniflow.R;
import com.student.uniflow.model.Task;

import java.util.List;

public class RecentTaskAdapter extends RecyclerView.Adapter<RecentTaskAdapter.ViewHolder> {

    private final Context context;
    private List<Task> taskList;

    public RecentTaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recent_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvSubject.setText(task.getSubject() != null && !task.getSubject().isEmpty()
                ? task.getSubject() : "No Subject");
        holder.tvDeadline.setText(task.getDeadline() != null && !task.getDeadline().isEmpty()
                ? task.getDeadline() : "-");
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubject, tvDeadline;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRecentTitle);
            tvSubject = itemView.findViewById(R.id.tvRecentSubject);
            tvDeadline = itemView.findViewById(R.id.tvRecentDeadline);
        }
    }
}
