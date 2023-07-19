package com.example.scuolaguida.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.CaseMap;
import android.media.metrics.Event;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.scuolaguida.R;
import com.example.scuolaguida.models.AlarmReceiver;
import com.example.scuolaguida.models.CalendarProvider;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.models.MyWorker;
import com.example.scuolaguida.ui.home.HomeFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<MyEvent> lessons;

    public HomeAdapter(List<MyEvent> L) {
        this.lessons = L;
    }

    private AdapterView.OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView capitoloID;
        private long eventID;
        String TAG = "MyWorkerTag";


        private EventListAdapter adapter;
        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

        public ViewHolder(View view) {
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.capitoloID = (TextView) view.findViewById(R.id.capitoloID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            TextView bottoneannulla = view.findViewById(R.id.bottone_ANNULLA);
            TextView bottonecalendario = view.findViewById(R.id.addtocalendar);

            bottonecalendario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String capitolo = getCapitoloID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String userId = auth.getUid();
                    AddToCalendar(view.getContext(), giorno, mese, anno, capitolo, orario);

                    // AggiungiCalendario(view.getContext(),giorno, mese, anno, capitolo, orario);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(view.getContext().getString(R.string.titolo_aggiuntocalendario));
                    builder.setMessage(view.getContext().getString(R.string.contenuto_calendario));
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

            bottoneannulla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //homeFragment.setCancella(true);
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String capitolo = getCapitoloID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String userId = auth.getUid();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    String lezioneid = giorno + "-" + mese + "-" + anno + "-" + capitolo + "-" + orario;
                    DatabaseReference userRef = databaseRef.child("users").child(userId).child(lezioneid);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(view.getContext().getString(R.string.titolo_elimina));
                    builder.setMessage(view.getContext().getString(R.string.contenuto_elimina));
                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userRef.removeValue();

                            //rimuovi da calendario
                            String[] ora_minuti = orario.split(":");
                            String ora = ora_minuti[0];
                            String minuti = ora_minuti[1];

                            int annoint = Integer.parseInt(anno);
                            int meseint = Integer.parseInt(mese);
                            int giornoint = Integer.parseInt(giorno);
                            int oraint = Integer.parseInt(ora);
                            int minutint = Integer.parseInt(minuti);
                            //RemoveFromCalendar(view.getContext(), eventID);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        public void AddToCalendar(Context context, String giorno, String mese, String anno, String capitolo, String orario) {
            String[] ora_minuti = orario.split(":");
            String ora = ora_minuti[0];
            String minuti = ora_minuti[1];

            int annoint = Integer.parseInt(anno);
            int meseint = Integer.parseInt(mese);
            int giornoint = Integer.parseInt(giorno);
            int oraint = Integer.parseInt(ora);
            int minutint = Integer.parseInt(minuti);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, annoint);
            calendar.set(Calendar.MONTH, meseint - 1);
            calendar.set(Calendar.DAY_OF_MONTH, giornoint);
            calendar.set(Calendar.HOUR_OF_DAY, oraint);
            calendar.set(Calendar.MINUTE, minutint);
            calendar.set(Calendar.SECOND, 0);
            long starttime = calendar.getTimeInMillis();
            long endtime = starttime + (60 * 60 * 1000);
            CalendarProvider calendarProvider = new CalendarProvider(context.getContentResolver());
            boolean isEventAdded = calendarProvider.writeEventToCalendar(context, "lezione scuolaguida",
                    "capitolo della lezione :  " + capitolo, starttime, endtime);

            // Imposta l'allarme per 30 minuti prima dell'evento
            long anticipo = 30 * 60 * 1000;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, starttime - anticipo, pendingIntent);

            /*String[] projection = new String[]{CalendarContract.Events._ID};
            String selection = CalendarContract.Events.TITLE + "=? AND " +
                    CalendarContract.Events.DESCRIPTION + "=? AND " +
                    CalendarContract.Events.DTSTART + "=? AND " +
                    CalendarContract.Events.DTEND + "=?";
            String[] selectionArgs = new String[]{"lezione scuolaguida", "capitolo della lezione : " + capitolo, String.valueOf(starttime), String.valueOf(endtime)};

            Cursor cursor = context.getContentResolver().query(
                    CalendarContract.Events.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            eventID = -1; // Valore di default se l'evento non viene trovato

            if (cursor != null && cursor.moveToFirst()) {
                int eventIdColumnIndex = cursor.getColumnIndex(CalendarContract.Events._ID);
                eventID = cursor.getLong(eventIdColumnIndex);
                cursor.close();
            }

            return eventID;*/
        }

        /*public void RemoveFromCalendar(Context context, long eventId) {
            ContentResolver contentResolver = context.getContentResolver();
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);

            int rows = contentResolver.delete(deleteUri, null, null);
            if (rows > 0) {
                Toast.makeText(context, "Evento eliminato con successo", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Errore durante l'eliminazione dell'evento", Toast.LENGTH_SHORT).show();
            }
        }*/


        public static void deleteEventFromCalendar(Context context, String giorno, String mese, String anno, String orario) {
            String[] ora_minuti = orario.split(":");
            String ora = ora_minuti[0];
            String minuti = ora_minuti[1];

            int annoint = Integer.parseInt(anno);
            int meseint = Integer.parseInt(mese);
            int giornoint = Integer.parseInt(giorno);
            int oraint = Integer.parseInt(ora);
            int minutint = Integer.parseInt(minuti);
            ContentResolver contentResolver = context.getContentResolver();
            String[] projection = new String[]{
                    CalendarContract.Events._ID,
                    CalendarContract.Events.TITLE,
                    CalendarContract.Events.DTSTART
            };

            // Imposta la data di inizio e fine dell'evento per eseguire la query
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(giornoint, meseint, annoint, oraint, minutint);
            long startTimeInMillis = startCalendar.getTimeInMillis();

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(startTimeInMillis + (60 * 60 * 1000)); // Imposta l'ora di fine 1 ora dopo l'ora di inizio

            // Query per gli eventi che corrispondono al periodo di tempo specificato
            Cursor cursor = contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    projection,
                    CalendarContract.Events.DTSTART + ">=? AND " + CalendarContract.Events.DTEND + "<?",
                    new String[]{String.valueOf(startTimeInMillis), String.valueOf(endCalendar.getTimeInMillis())},
                    null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") long eventId = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID));
                    ContentResolver contextContentResolver = context.getContentResolver();
                    Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                    int rows = contextContentResolver.delete(deleteUri, null, null);

                    if (rows > 0) {
                        Toast.makeText(context, "evento eliminato con successo", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "fallitooooooo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            cursor.close();
        }


    public TextView getGiornoID() {
        return giornoID;
    }

    public TextView getCapitoloID() {
        return capitoloID;
    }

    public TextView getOrarioID() {
        return orarioID;
    }

    public TextView getMeseID() {
        return meseID;
    }

    public TextView getAnnoID() {
        return annoID;
    }
}

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lezioni_home, viewGroup, false);

        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getGiornoID().setText(String.valueOf(this.lessons.get(position).getGiorno()));
        viewHolder.getOrarioID().setText(String.valueOf(this.lessons.get(position).getOrario()));
        viewHolder.getCapitoloID().setText(String.valueOf(this.lessons.get(position).getCapitolo()));
        viewHolder.getMeseID().setText(String.valueOf(this.lessons.get(position).getMese()));
        viewHolder.getAnnoID().setText(String.valueOf(this.lessons.get(position).getAnno()));
    }


    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<MyEvent> lessonList) {
        this.lessons = lessonList;
    }
}
