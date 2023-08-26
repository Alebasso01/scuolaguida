package com.example.scuolaguida.adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.models.AlarmReceiver;
import com.example.scuolaguida.models.CalendarProvider;
import com.example.scuolaguida.models.EventPratica;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class PraticaHomeAdapter extends RecyclerView.Adapter<PraticaHomeAdapter.ViewHolder> {

    private List<EventPratica> lessons;

    public PraticaHomeAdapter(List<EventPratica> L) {
        this.lessons = L;
    }
    private AdapterView.OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView istruttoreID;
        private final TextView veicoloID;
        private final TextView targaID;
        private final TextView patenteID;
        private final TextView tipoID;
        private long eventID;
        String TAG = "MyWorkerTag";

        private EventListAdapter adapter;
        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

        public ViewHolder(View view) {
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            this.istruttoreID = (TextView) view.findViewById(R.id.istruttoreID);
            this.veicoloID = (TextView) view.findViewById(R.id.veicoloID);
            this.targaID = (TextView) view.findViewById(R.id.targaID);
            this.tipoID = (TextView) view.findViewById(R.id.tipoID);
            this.patenteID = (TextView) view.findViewById(R.id.patenteID);
            TextView bottoneannulla = view.findViewById(R.id.bottone_ANNULLA);
            TextView bottonecalendario = view.findViewById(R.id.addtocalendar);

            bottonecalendario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String veicolo = getVeicoloID().getText().toString();
                    String istruttore = getIstruttoreID().getText().toString();
                    String targa = getTargaID().getText().toString();
                    String tipo = getTipoID().getText().toString();
                    String patente = getPatenteID().getText().toString();

                    String userId = auth.getUid();
                    AddToCalendar(view.getContext(), giorno, mese, anno, veicolo, istruttore, orario);

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
                    String veicolo = getVeicoloID().getText().toString();
                    String istruttore = getIstruttoreID().getText().toString();
                    String targa = getTargaID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String userId = auth.getUid();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    String lezioneid = giorno + "-" + mese + "-" + anno + "-" + targa + "-" + orario;
                    DatabaseReference userRef = databaseRef.child("users").child(userId).child("pratica").child(lezioneid);
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

        public void AddToCalendar(Context context, String giorno, String mese, String anno, String veicolo, String istruttore, String orario) {
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
                    "istruttore della lezione :  " + istruttore, starttime, endtime);

            // Imposta l'allarme per 30 minuti prima dell'evento
            long anticipo = 30 * 60 * 1000;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, starttime - anticipo, pendingIntent);
        }

            public TextView getGiornoID() {
                return giornoID;
            }

            public TextView getIstruttoreID() {
                return istruttoreID;
            }
            public TextView getVeicoloID() {
            return veicoloID;
            }
            public TextView getTargaID() {
            return targaID;
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
            public TextView getTipoID() {
            return tipoID;
        }
            public TextView getPatenteID() {
            return patenteID;
        }
        }

    @NonNull
    @Override
    public PraticaHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lezioni_pratica_home, viewGroup, false);

        return new PraticaHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PraticaHomeAdapter.ViewHolder viewHolder, int position) {
            viewHolder.getGiornoID().setText(String.valueOf(this.lessons.get(position).getGiorno()));
            viewHolder.getOrarioID().setText(String.valueOf(this.lessons.get(position).getOrario()));
            viewHolder.getMeseID().setText(String.valueOf(this.lessons.get(position).getMese()));
            viewHolder.getAnnoID().setText(String.valueOf(this.lessons.get(position).getAnno()));
            viewHolder.getIstruttoreID().setText(String.valueOf(this.lessons.get(position).getIstruttore()));
            viewHolder.getTargaID().setText(String.valueOf(this.lessons.get(position).getTarga()));
            viewHolder.getVeicoloID().setText(String.valueOf(this.lessons.get(position).getVeicolo()));
            viewHolder.getTipoID().setText(String.valueOf(this.lessons.get(position).getTipo()));
            viewHolder.getPatenteID().setText(String.valueOf(this.lessons.get(position).getPatente()));
    }

    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<EventPratica> lessonList) {
        this.lessons = lessonList;
    }
}
