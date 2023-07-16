package com.example.scuolaguida.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                    Toast.makeText(view.getContext(), "prova",Toast.LENGTH_SHORT);

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    String lezioneid = giorno+"-"+mese+"-"+anno+"-"+capitolo+"-"+orario;
                    DatabaseReference userRef = databaseRef.child("users").child(userId).child(lezioneid);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Conferma eliminazione prenotazione");
                    builder.setMessage("Sei sicuro di voler cancellare la tua prenotazione?");
                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userRef.removeValue();
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
        public void AddToCalendar(Context context, String giorno, String mese, String anno, String capitolo, String orario){
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
            boolean isEventAdded = calendarProvider.writeEventToCalendar(context, "nuova lezione",
                    "capitolo della lezione :  " + capitolo, starttime, endtime);

            /*Calendar notificationCalendar = (Calendar) calendar.clone();
            int notificationDay = notificationCalendar.get(Calendar.DAY_OF_MONTH);
            int notificationMonth = notificationCalendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
            int notificationYear = notificationCalendar.get(Calendar.YEAR);
            int notificationHour = notificationCalendar.get(Calendar.HOUR_OF_DAY);
            int notificationMinute = notificationCalendar.get(Calendar.MINUTE);

            Data inputData = new Data.Builder()
                    .putInt("notificationDay", notificationDay)
                    .putInt("notificationMonth", notificationMonth)
                    .putInt("notificationYear", notificationYear)
                    .putInt("notificationHour", notificationHour)
                    .putInt("notificationMinute", notificationMinute)
                    .build();

            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,
                    30, TimeUnit.SECONDS,
                    30, TimeUnit.SECONDS).addTag(TAG).setInitialDelay(0, TimeUnit.HOURS).build();


            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(inputData)
                    .build();

            WorkManager.getInstance(context).enqueue(workRequest);
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG);*/


        }
        public void RemoveFromCalendar(Context context, String giorno, String mese, String anno, String orario) {
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

            CalendarProvider calendarProvider = new CalendarProvider(context.getContentResolver());
            boolean isEventRemoved = calendarProvider.removeEventFromCalendar(context, starttime);
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
