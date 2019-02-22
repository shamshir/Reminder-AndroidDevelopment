package com.javi.android.reminder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID id;
    private String title;
    private Date date;
    private String priority;
    private boolean done;

    public Task() {

        this(UUID.randomUUID());
    }

    public Task(UUID id) {

        this.id = id;
        date = new Date();
        priority = "bnormal";
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getFormatedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String convertedDate = dateFormat.format(date);
        return convertedDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
