package com.javi.android.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.javi.android.reminder.database.TaskBaseHelper;
import com.javi.android.reminder.database.TaskCursorWrapper;
import com.javi.android.reminder.database.TaskDbSchema.TaskTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskCollection {

    private static TaskCollection taskCollection;

    private Context thisContext;
    private SQLiteDatabase database;

    private TaskCollection(Context context) {

        thisContext = context.getApplicationContext();
        database = new TaskBaseHelper(thisContext).getWritableDatabase();
    }

    public static TaskCollection get(Context context) {

        if (taskCollection == null) {
            taskCollection = new TaskCollection(context);
        }
        return taskCollection;
    }

    public void addTask(Task task) {

        ContentValues values = getContentValues(task);

        database.insert(TaskTable.NAME, null, values);
    }

    public List<Task> getTasks(String orderBy) {

        String orderByClause = null;
        if (orderBy.equals("byPriority")) {
            orderByClause = TaskTable.Cols.DONE + ", " + TaskTable.Cols.PRIORITY + ", " + TaskTable.Cols.DATE;
        }
        if (orderBy.equals("byDate")) {
            orderByClause = TaskTable.Cols.DONE + ", " + TaskTable.Cols.DATE + ", " + TaskTable.Cols.PRIORITY;
        }

        List<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = queryTasks(null, null, orderByClause);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return tasks;
    }

    public Task getTask(UUID id) {

        TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.UUID + " = ?",
                new String[] { id.toString() },
                null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void updateTask(Task task) {

        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);

        database.update(
                TaskTable.NAME,
                values,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteTask(Task task) {

        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);

        database.delete(TaskTable.NAME,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteCompletedTasks() {

        database.delete(TaskTable.NAME,
                TaskTable.Cols.DONE + " = " + 1,
                null);
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs, String orderByClause) {

        Cursor cursor = database.query(
                TaskTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderByClause
        );

        return new TaskCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Task task) {

        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.DATE, task.getDate().getTime());
        values.put(TaskTable.Cols.PRIORITY, task.getPriority());
        values.put(TaskTable.Cols.DONE, task.isDone() ? 1 : 0);

        return values;
    }
}
