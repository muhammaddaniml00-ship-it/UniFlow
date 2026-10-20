package com.student.uniflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.student.uniflow.model.Task;
import com.student.uniflow.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "uniflow.db";
    private static final int DATABASE_VERSION = 1;

    // Tasks table
    public static final String TABLE_TASKS = "tasks";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_DEADLINE = "deadline";
    public static final String COL_COMPLETED = "completed";
    public static final String COL_CREATED_AT = "created_at";

    // Profile table
    public static final String TABLE_PROFILE = "profile";
    public static final String COL_PROFILE_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_NIM = "nim";
    public static final String COL_PROGRAM = "program";

    private static final String CREATE_TASKS_TABLE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_SUBJECT + " TEXT, " +
            COL_DEADLINE + " TEXT, " +
            COL_COMPLETED + " INTEGER DEFAULT 0, " +
            COL_CREATED_AT + " INTEGER" +
            ");";

    private static final String CREATE_PROFILE_TABLE =
            "CREATE TABLE " + TABLE_PROFILE + " (" +
            COL_PROFILE_ID + " INTEGER PRIMARY KEY, " +
            COL_NAME + " TEXT, " +
            COL_NIM + " TEXT, " +
            COL_PROGRAM + " TEXT" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        onCreate(db);
    }

    // ==================== TASK OPERATIONS ====================

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_SUBJECT, task.getSubject());
        values.put(COL_DEADLINE, task.getDeadline());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COL_CREATED_AT, System.currentTimeMillis());
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_SUBJECT, task.getSubject());
        values.put(COL_DEADLINE, task.getDeadline());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);
        int rows = db.update(TABLE_TASKS, values, COL_ID + "=?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_TASKS, COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null,
                COL_COMPLETED + " ASC, " + COL_CREATED_AT + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tasks.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> getRecentTasks(int limit) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COL_COMPLETED + "=0",
                null, null, null,
                COL_CREATED_AT + " DESC",
                String.valueOf(limit));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tasks.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public int getTotalTaskCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    public int getCompletedTaskCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS +
                " WHERE " + COL_COMPLETED + "=1", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    public int getPendingTaskCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS +
                " WHERE " + COL_COMPLETED + "=0", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
        task.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT)));
        task.setDeadline(cursor.getString(cursor.getColumnIndexOrThrow(COL_DEADLINE)));
        task.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPLETED)) == 1);
        task.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)));
        return task;
    }

    // ==================== PROFILE OPERATIONS ====================

    public void saveProfile(UserProfile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROFILE_ID, 1);
        values.put(COL_NAME, profile.getName());
        values.put(COL_NIM, profile.getNim());
        values.put(COL_PROGRAM, profile.getProgram());

        Cursor cursor = db.query(TABLE_PROFILE, null, COL_PROFILE_ID + "=1",
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            db.update(TABLE_PROFILE, values, COL_PROFILE_ID + "=1", null);
        } else {
            db.insert(TABLE_PROFILE, null, values);
        }
        if (cursor != null) cursor.close();
        db.close();
    }

    public UserProfile getProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILE, null, COL_PROFILE_ID + "=1",
                null, null, null, null);
        UserProfile profile = null;
        if (cursor != null && cursor.moveToFirst()) {
            profile = new UserProfile(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NIM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PROGRAM))
            );
            cursor.close();
        }
        db.close();
        return profile;
    }
}
