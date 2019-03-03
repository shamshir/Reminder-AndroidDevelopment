package com.javi.android.reminder.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.javi.android.reminder.R;
import com.javi.android.reminder.TaskListActivity;

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

        triggerNotification();

        return Worker.Result.success();
    }

    private void triggerNotification() {

        /* Notification Channel Creation - Only on API 26 and above */
        String channelName = "Reminder Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        /* Pending Intent Creation */
        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

        /* Notification Creation */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelName)
                .setSmallIcon(R.drawable.ic_brain)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.brain_launcher_round))
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
