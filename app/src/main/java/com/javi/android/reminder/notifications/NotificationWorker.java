package com.javi.android.reminder.notifications;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    private String notificationTitle;
    private String notificationText;
    private int notificationId;

    public NotificationWorker(Context context, WorkerParameters params) {

        super(context, params);

        /* Obtain Input Data (Notification Title and Text) */
        String[] notificationData = getInputData().getStringArray("taskData");
        notificationTitle = notificationData[0];
        notificationText = notificationData[1];
        /* Calculate Random Id to avoid Notification Overwriting */
        notificationId = (int) System.currentTimeMillis();
    }

    @Override
    public Worker.Result doWork() {

        new TaskNotification(getApplicationContext(), notificationTitle, notificationText, notificationId);

        return Worker.Result.success();
    }
}
