package com.student.uniflow.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.student.uniflow.R;
import com.student.uniflow.adapter.TaskAdapter;
import com.student.uniflow.database.DatabaseHelper;
import com.student.uniflow.model.Task;

import java.util.Calendar;
import java.util.List;

public class TasksFragment extends Fragment implements TaskAdapter.OnTaskActionListener {

    private DatabaseHelper dbHelper;
    private RecyclerView rvTasks;
    private TaskAdapter taskAdapter;
    private View emptyStateView;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());

        rvTasks = view.findViewById(R.id.rvTasks);
        emptyStateView = view.findViewById(R.id.layoutEmptyState);
        fabAdd = view.findViewById(R.id.fabAddTask);

        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Task> tasks = dbHelper.getAllTasks();
        taskAdapter = new TaskAdapter(requireContext(), tasks, this);
        rvTasks.setAdapter(taskAdapter);

        fabAdd.setOnClickListener(v -> showTaskDialog(null));

        refreshTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTasks();
    }

    private void refreshTasks() {
        List<Task> tasks = dbHelper.getAllTasks();
        if (taskAdapter != null) taskAdapter.setTaskList(tasks);
        if (tasks.isEmpty()) {
            rvTasks.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            rvTasks.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    private void showTaskDialog(@Nullable Task existingTask) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_task, null);

        EditText etTitle = dialogView.findViewById(R.id.etTaskTitle);
        EditText etSubject = dialogView.findViewById(R.id.etTaskSubject);
        EditText etDeadline = dialogView.findViewById(R.id.etTaskDeadline);
        Button btnPickDate = dialogView.findViewById(R.id.btnPickDate);

        final String[] selectedDate = {""};

        if (existingTask != null) {
            etTitle.setText(existingTask.getTitle());
            etSubject.setText(existingTask.getSubject());
            etDeadline.setText(existingTask.getDeadline());
            if (existingTask.getDeadline() != null) {
                selectedDate[0] = existingTask.getDeadline();
            }
        }

        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (dp, year, month, day) -> {
                String dateStr = String.format("%02d/%02d/%04d", day, month + 1, year);
                selectedDate[0] = dateStr;
                etDeadline.setText(dateStr);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        String title = existingTask == null ? "Tambah Tugas" : "Edit Tugas";
        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    String taskTitle = etTitle.getText().toString().trim();
                    String subject = etSubject.getText().toString().trim();
                    String deadline = etDeadline.getText().toString().trim();

                    if (TextUtils.isEmpty(taskTitle)) {
                        Toast.makeText(requireContext(),
                                "Nama tugas tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (existingTask == null) {
                        Task newTask = new Task();
                        newTask.setTitle(taskTitle);
                        newTask.setSubject(subject);
                        newTask.setDeadline(deadline);
                        newTask.setCompleted(false);
                        dbHelper.insertTask(newTask);
                        Toast.makeText(requireContext(), "Tugas ditambahkan!", Toast.LENGTH_SHORT).show();
                    } else {
                        existingTask.setTitle(taskTitle);
                        existingTask.setSubject(subject);
                        existingTask.setDeadline(deadline);
                        dbHelper.updateTask(existingTask);
                        Toast.makeText(requireContext(), "Tugas diperbarui!", Toast.LENGTH_SHORT).show();
                    }
                    refreshTasks();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    public void onEdit(Task task) {
        showTaskDialog(task);
    }

    @Override
    public void onDelete(Task task) {
        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle("Hapus Tugas")
                .setMessage("Yakin ingin menghapus tugas \"" + task.getTitle() + "\"?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    dbHelper.deleteTask(task.getId());
                    Toast.makeText(requireContext(), "Tugas dihapus!", Toast.LENGTH_SHORT).show();
                    refreshTasks();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    public void onToggleComplete(Task task, boolean checked) {
        task.setCompleted(checked);
        dbHelper.updateTask(task);
        refreshTasks();
    }
}
