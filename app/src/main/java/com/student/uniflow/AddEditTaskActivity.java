package com.student.uniflow;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.student.uniflow.database.DatabaseHelper;
import com.student.uniflow.model.Task;

import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "task_id";

    private EditText etTitle, etSubject, etDeadline;
    private Button btnSave, btnPickDate;
    private DatabaseHelper dbHelper;
    private long taskId = -1;
    private Task existingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        dbHelper = new DatabaseHelper(this);
        etTitle = findViewById(R.id.etTitle);
        etSubject = findViewById(R.id.etSubject);
        etDeadline = findViewById(R.id.etDeadline);
        btnSave = findViewById(R.id.btnSave);
        btnPickDate = findViewById(R.id.btnPickDate);

        if (getIntent().hasExtra(EXTRA_TASK_ID)) {
            taskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
        }

        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (dp, year, month, day) -> {
                String date = String.format("%02d/%02d/%04d", day, month + 1, year);
                etDeadline.setText(date);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> saveTask());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(taskId == -1 ? "Tambah Tugas" : "Edit Tugas");
        }
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Nama tugas tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task();
        task.setTitle(title);
        task.setSubject(subject);
        task.setDeadline(deadline);

        if (taskId == -1) {
            task.setCompleted(false);
            dbHelper.insertTask(task);
            Toast.makeText(this, "Tugas ditambahkan!", Toast.LENGTH_SHORT).show();
        } else {
            task.setId(taskId);
            dbHelper.updateTask(task);
            Toast.makeText(this, "Tugas diperbarui!", Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
