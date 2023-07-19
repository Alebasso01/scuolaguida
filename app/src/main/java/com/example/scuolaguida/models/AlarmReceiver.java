package com.example.scuolaguida.models;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "YourChannelId"; // ID del canale di notifica

    @Override
    public void onReceive(Context context, Intent intent) {
        // Creazione di un canale di notifica (necessario per Android Oreo e versioni successive)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence channelName = "YourChannelName"; // Nome del canale di notifica
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_macchina_bianca)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_macchina_bianca))
                .setContentTitle("Lezione scuolaguida")
                .setContentText("La tua prossima lezione inizierà tra 30 minuti!")
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setColorized(true)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 1000, 1000)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);


        // Mostra la notifica
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build()); // Il secondo argomento rappresenta un ID univoco per la notifica (puoi cambiarlo se vuoi mostrare più notifiche diverse)
    }
}

