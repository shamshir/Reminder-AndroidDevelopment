package com.javi.android.reminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        date = newDate();
        priority = "bmedium";
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

    public String getFormatedTime() {

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String convertedTime = timeFormat.format(date);
        return convertedTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.setTime(this.date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);

        this.date = c.getTime();
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

    private Date newDate() {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date d = c.getTime();

        return d;
    }
}
