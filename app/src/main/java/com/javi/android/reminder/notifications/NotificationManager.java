package com.javi.android.reminder.notifications;

import android.content.Context;

import com.javi.android.reminder.Task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NotificationManager {

    public static void stopNotifications(Task task) {

        /* Obtain Task Unique Tag from UUID */
        String tag = task.getId().toString();

        /* Cancel Notifications with Specified Tag */
        WorkManager.getInstance().cancelAllWorkByTag(tag);
    }

    public static void scheduleNotification(Task task) {

        /* Obtain Task Unique Tag from UUID */
        String tag = task.getId().toString();

        /* Set Input Data (Notification Title and Text) */
        Data.Builder inputData = new Data.Builder();
        inputData.putStringArray("taskData", new String[] {task.getTitle(), task.getFormatedDate() + "    " + task.getFormatedTime()});
        Data data = inputData.build();

        /* Calculate Time until Notification */
        long time = calculateTime(task.getDate());

        /* Create Notification Request */
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(tag)
                .build();

        /* Queue Notification (Only if Task is not Due) */
        if (time > 0) {

            WorkManager.getInstance().enqueue(notificationWork);
        }
    }

    public static void showNotification(Context context, Task task) {

        /* Obtain Notification Data */
        String notificationTitle = task.getTitle();
        String notificationText = task.getFormatedDate() + "    " + task.getFormatedTime();
        int notificationId = (int) System.currentTimeMillis();

        /* Create Task Notification */
        new TaskNotification(context, notificationTitle, notificationText, notificationId);
    }

    private static long calculateTime(Date taskDate) {

        /* Difference in Milliseconds from Date to Now */
        long timeInMilliseconds = taskDate.getTime() - new Date().getTime();
        return timeInMilliseconds;
    }
}
