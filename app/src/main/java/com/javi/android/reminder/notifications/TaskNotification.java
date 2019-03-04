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

public class TaskNotification {

    public TaskNotification(Context context, String notificationTitle, String notificationText, int notificationId) {

        /* Notification Channel Creation - Only on API 26 and above */
        String channelName = "Reminder Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
            android.app.NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        /* Pending Intent Creation */
        Intent intent = new Intent(context, TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);

        /* Notification Creation */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelName)
                .setSmallIcon(R.drawable.ic_brain)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.brain_launcher_round))
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
