package com.student.uniflow.model;

public class Task {
    private long id;
    private String title;
    private String subject;
    private String deadline;
    private boolean completed;
    private long createdAt;

    public Task() {}

    public Task(long id, String title, String subject, String deadline, boolean completed, long createdAt) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.deadline = deadline;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
