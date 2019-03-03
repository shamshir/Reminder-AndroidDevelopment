package com.javi.android.reminder.notifications;

import com.javi.android.reminder.Task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NotificationManager {

    public static void stopNotifications(String tag) {

        /* Cancel Notifications with Specified Tag */
        WorkManager.getInstance().cancelAllWorkByTag(tag);
    }

    public static void scheduleNotification(Task task, String tag) {

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

        /* Queue Notification */
        WorkManager.getInstance().enqueue(notificationWork);
    }

    private static long calculateTime(Date taskDate) {

        long timeInMilliseconds = taskDate.getTime() - new Date().getTime();
        return timeInMilliseconds;
    }
}
