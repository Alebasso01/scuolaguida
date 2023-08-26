package com.example.scuolaguida.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.EnterActivity;
import com.example.scuolaguida.activities.MainActivity;
import com.example.scuolaguida.fragments.prenotazioni.PrenotazioniFragment;
import com.example.scuolaguida.models.CalendarProvider;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.models.MyWorker;
import com.example.scuolaguida.models.PermissionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.Calendar;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<MyEvent> lessons;
    private Context context;
    public EventListAdapter(List<MyEvent> L, Context c) {
        this.lessons = L;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView capitoloID;
        private final TextView tipoID;
        private final TextView patenteID;
        private final TextView capienzaID;
        private final TextView capienzaattualeID;

        private TextView descrizione;
        String cap;

        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
        private MyWorker worker;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TextView bottoneprenotazioni;

        CardView cardView = itemView.findViewById(R.id.cardview);

        public ViewHolder(View view) {

            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.capitoloID = (TextView) view.findViewById(R.id.capitoloID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            this.tipoID = (TextView) view.findViewById(R.id.tipoID);
            this.capienzaID = (TextView) view.findViewById(R.id.capienzaID);
            this.capienzaattualeID = (TextView) view.findViewById(R.id.capienzaattualeID);
            this.patenteID = (TextView) view.findViewById(R.id.patenteID);
            bottoneprenotazioni = view.findViewById(R.id.bottonenuovaprenotazione);
            descrizione = view.findViewById(R.id.stringadescrizione);

            bottoneprenotazioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String capitolo = getCapitoloID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String tipo = getTipoID().getText().toString();
                    String patente = getPatenteID().getText().toString();
                    String  capienza = getCapienzaID().getText().toString();
                    String capienzaattuale = getCapienzaattualeID().getText().toString();
                    String userId = auth.getUid();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    DatabaseReference lesRef = databaseRef.child("AllLessons");
                    String lezioneid = giorno+"-"+mese+"-"+anno+"-"+capitolo+"-"+orario;
                    DatabaseReference userRef_teoria = databaseRef.child("users").child(userId).child("teoria").child(lezioneid);
                    DatabaseReference userRef_pratica = databaseRef.child("users").child(userId).child("pratica").child(lezioneid);
                    DatabaseReference Ref = databaseRef.child("users").child(userId);

                    userRef_teoria.child("giorno").setValue(giorno);
                    userRef_teoria.child("mese").setValue(mese);
                    userRef_teoria.child("anno").setValue(anno);
                    userRef_teoria.child("capitolo").setValue(capitolo);
                    userRef_teoria.child("orario").setValue(orario);
                    userRef_teoria.child("tipo").setValue(tipo);
                    userRef_teoria.child("patente").setValue(patente);
                    lesRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                            /*Log.d("giorno :  ", "giornoFromDatabase: " + snapshot);
                            for (DataSnapshot data : snapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                        String giornoFromDatabase = snapshot3.child("giorno").getValue(String.class);
                                        String meseFromDatabase = snapshot3.child("mese").getValue(String.class);
                                        String annoFromDatabase = snapshot3.child("anno").getValue(String.class);
                                        String capitoloFromDatabase = snapshot3.child("capitolo").getValue(String.class);
                                        String orarioFromDatabase = snapshot3.child("orario").getValue(String.class);
                                        String patenteFromDatabase = snapshot3.child("patente").getValue(String.class);
                                        String tipoFromDatabase = snapshot3.child("tipo").getValue(String.class);

                                        String giornoFromTextView = getGiornoID().getText().toString();
                                        String meseFromTextView = getMeseID().getText().toString();
                                        String annoFromTextView = getAnnoID().getText().toString();
                                        String capitoloFromTextView = getCapitoloID().getText().toString();
                                        String orarioFromTextView = getOrarioID().getText().toString();
                                        String patenteFromTextView = getPatenteID().getText().toString();
                                        String tipoFromTextView = getTipoID().getText().toString();



                                        if (giornoFromDatabase != null && meseFromDatabase != null && annoFromDatabase != null &&
                                                capitoloFromDatabase != null && orarioFromDatabase != null &&
                                                patenteFromDatabase != null && tipoFromDatabase != null &&
                                                giornoFromDatabase.equals(giornoFromTextView) &&
                                                meseFromDatabase.equals(meseFromTextView) &&
                                                annoFromDatabase.equals(annoFromTextView) &&
                                                capitoloFromDatabase.equals(capitoloFromTextView) &&
                                                orarioFromDatabase.equals(orarioFromTextView) &&
                                                patenteFromDatabase.equals(patenteFromTextView) &&
                                                tipoFromDatabase.equals(tipoFromTextView)) {

                                            DatabaseReference capienzaAttualeRef = snapshot3.getRef().child("capienzaattuale");
                                            int capienzaAttuale = Integer.parseInt(capienzaattuale);
                                            capienzaAttualeRef.setValue(capienzaAttuale - 1);*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(view.getContext().getString(R.string.titolo_prenotazione));
                    builder.setMessage(view.getContext().getString(R.string.contenuto_prenotazione));
                    AlertDialog dialog = builder.create();
                    dialog.show();

                   /* userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChildren()){
                                Toast.makeText(view.getContext(), "users esistono", Toast.LENGTH_SHORT).show();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Toast.makeText(view.getContext(), ""+snapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    if(data.getKey().equals(auth.getUid()) && data.hasChildren()){
                                        for (DataSnapshot lesson : data.getChildren()) {
                                            MyEvent event = lesson.getValue(MyEvent.class);
                                            Toast.makeText(view.getContext(), ""+Integer.parseInt(event.getAnno()), Toast.LENGTH_SHORT).show();
                                            if (event.getAnno() != null && !event.getAnno().isEmpty()) {
                                                int aa = Integer.parseInt(event.getAnno());
                                                int mm = Integer.parseInt(event.getMese());
                                                int gg = Integer.parseInt(event.getGiorno());
                                                String orario = event.getOrario();
                                                String[] ora_minuti = orario.split(":");
                                                String ora = ora_minuti[0];
                                                String minuti = ora_minuti[1];
                                                int oraint = Integer.parseInt(ora);
                                                int minutint = Integer.parseInt(minuti);
                                            }

                                        }
                                    }
                                }
                            }
                            else{
                                Toast.makeText(view.getContext(), "users non esistono", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                }
            });
        }

        public TextView getGiornoID(){return giornoID;}
        public TextView getCapitoloID(){return capitoloID;}
        public TextView getOrarioID(){return orarioID;}
        public TextView getMeseID(){return meseID;}
        public TextView getAnnoID(){return annoID;}
        public TextView getTipoID(){return tipoID;}
        public TextView getPatenteID(){return patenteID;}
        public TextView getCapienzaID(){return capienzaID;}
        public TextView getCapienzaattualeID(){return capienzaattualeID;}
        public void setDescrizione(String string) {
            descrizione.setText(string);
        }



        public void SendNotification(){
            Intent intent = new Intent(itemView.getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(itemView.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Costruisci la notifica utilizzando la classe NotificationCompat
            NotificationCompat.Builder builder = new NotificationCompat.Builder(itemView.getContext(), "channel_id")
                    .setSmallIcon(R.drawable.ic_menu_gallery)
                    .setLargeIcon(BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_menu_gallery))
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
            NotificationManager notificationManager = (NotificationManager) itemView.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());}
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lezioni_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getGiornoID().setText(String.valueOf(this.lessons.get(position).getGiorno()));
        viewHolder.getOrarioID().setText(String.valueOf(this.lessons.get(position).getOrario()));
        viewHolder.getCapitoloID().setText(String.valueOf(this.lessons.get(position).getCapitolo()));
        viewHolder.getMeseID().setText(String.valueOf(this.lessons.get(position).getMese()));
        viewHolder.getAnnoID().setText(String.valueOf(this.lessons.get(position).getAnno()));
        viewHolder.getTipoID().setText(String.valueOf(this.lessons.get(position).getTipo()));
        viewHolder.getPatenteID().setText(String.valueOf(this.lessons.get(position).getPatente()));
        viewHolder.getCapienzaID().setText(String.valueOf(this.lessons.get(position).getCapienza()));
        viewHolder.getCapienzaattualeID().setText(String.valueOf(this.lessons.get(position).getCapienzaattuale()));

        TextView capitoloIDTextView = viewHolder.getCapitoloID();
        String capitoloIDString = capitoloIDTextView.getText().toString();
        int capitoloint = Integer.parseInt(capitoloIDString);

        if (capitoloint == 1) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo1_patenteAB));
        } else if (capitoloint == 2) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo2_patenteAB));
        } else if (capitoloint == 3) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo3_patenteAB));
        } else if (capitoloint == 4) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo4_patenteAB));
        } else if (capitoloint == 5) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo5_patenteAB));
        } else if (capitoloint == 6) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo6_patenteAB));
        } else if (capitoloint == 7) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo7_patenteAB));
        } else if (capitoloint == 8) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo8_patenteAB));
        } else if (capitoloint == 9) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo9_patenteAB));
        } else if (capitoloint == 10) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo10_patenteAB));
        } else if (capitoloint == 11) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo11_patenteAB));
        } else if (capitoloint == 12) {
            viewHolder.setDescrizione(context.getString(R.string.capitolo12_patenteAB));
        }
    }


    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<MyEvent> lessonList) {
        this.lessons = lessonList;
    }
}