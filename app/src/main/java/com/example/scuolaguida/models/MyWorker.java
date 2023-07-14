package com.example.scuolaguida.models;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.MainActivity;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;
import java.util.logging.Handler;

public class MyWorker extends ListenableWorker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context,workerParameters);
    }
    FirebaseAuth auth;

    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
    int year = calendar.get(Calendar.YEAR);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    DatabaseReference userref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference().child("users").child(auth.getUid());

    public void SendNotification(){Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Costruisci la notifica utilizzando la classe NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_menu_gallery))
                .setContentTitle("Titolo della notifica")
                .setContentText("Testo della notifica")
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setColorized(true)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 1000, 1000)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Mostra la notifica utilizzando il NotificationManager
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());}
    private void checkUsers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Logic per controllare i dati degli utenti nel database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Esempio: stampa il nome di ogni utente
                    String name = userSnapshot.child("name").getValue(String.class);
                    Log.d("FirebaseService", "User: " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseService", "Errore durante la lettura del database Firebase: " + databaseError.getMessage());
            }
        });
    }
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Toast.makeText(getApplicationContext(), "ciaoooooooo", Toast.LENGTH_SHORT).show();
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    for (DataSnapshot lessonSnapshot : data.getChildren()) {
                        int anno = Integer.parseInt(lessonSnapshot.child("anno").getKey());
                        int mese = Integer.parseInt(lessonSnapshot.child("mese").getKey());
                        int giorno = Integer.parseInt(lessonSnapshot.child("giorno").getKey());
                        String orario = lessonSnapshot.child("orario").getKey();
                        String[] ora_minuti = orario.split(":");
                        int ora = Integer.parseInt(ora_minuti[0]);
                        int minuti = Integer.parseInt(ora_minuti[1]);
                        if (year == anno && mese == month && giorno == day && hour == ora && minute == minuti - 20) {
                            SendNotification();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}

