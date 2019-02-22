package com.javi.android.reminder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.javi.android.reminder.Task;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {

        super(cursor);
    }

    public Task getTask() {

        String uuidString = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TaskDbSchema.TaskTable.Cols.DATE));
        String priority = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.PRIORITY));
        int done = getInt(getColumnIndex(TaskDbSchema.TaskTable.Cols.DONE));

        Task task = new Task(UUID.fromString(uuidString));
        task.setTitle(title);
        task.setDate(new Date(date));
        task.setPriority(priority);
        task.setDone(done != 0);

        return task;
    }
}
