package com.example.scuolaguida.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.MainActivity;
import com.example.scuolaguida.models.CalendarProvider;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.ui.prenotazioni.PrenotazioniFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<MyEvent> lessons;

    public EventListAdapter(List<MyEvent> L){
        this.lessons = L;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView capitoloID;
        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

        Button bottoneprenotazioni;

        CardView cardView = itemView.findViewById(R.id.cardview);

        public ViewHolder(View view){
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.capitoloID = (TextView) view.findViewById(R.id.capitoloID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            bottoneprenotazioni = view.findViewById(R.id.bottone_prenotati);

            bottoneprenotazioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String giorno = getGiornoID().getText().toString();
                    String orario = getOrarioID().getText().toString();
                    String capitolo = getCapitoloID().getText().toString();
                    String mese = getMeseID().getText().toString();
                    String anno = getAnnoID().getText().toString();
                    String userId = auth.getUid();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference();
                    String lezioneid = giorno+"-"+mese+"-"+anno+"-"+capitolo+"-"+orario;
                    DatabaseReference userRef = databaseRef.child("users").child(userId).child(lezioneid);

                    userRef.child("giorno").setValue(giorno);
                    userRef.child("mese").setValue(mese);
                    userRef.child("anno").setValue(anno);
                    userRef.child("capitolo").setValue(capitolo);
                    userRef.child("orario").setValue(orario);
                    AddToCalendar(view.getContext(), giorno, mese, anno, capitolo, orario);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        bottoneprenotazioni.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }*/

                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                //String LezID = databaseRef.child("users").child(userId).child(lezioneid).getKey();
                                String LezID = childSnapshot.getValue().toString();
                                /*String[] lezioneidParts = LezID.split("-");
                                String giornoPart = lezioneidParts[0];
                                String mesePart = lezioneidParts[1];
                                String annoPart = lezioneidParts[2];
                                String capitoloPart = lezioneidParts[3];
                                String orarioPart = lezioneidParts[4];
                            if(giornoPart.equals(giorno) && mesePart.equals(mese) && annoPart.equals(anno
                            )       && capitoloPart.equals(capitolo) && orarioPart.equals(orario)) {
                                //elimina
                                //Toast.makeText(view.getContext(), ""+giorno+mese+anno, Toast.LENGTH_SHORT).show();
                                Toast.makeText(view.getContext(), ""+LezID, Toast.LENGTH_SHORT).show();
                               }
                            else{
                                //aggiungi
                                Toast.makeText(view.getContext(), "sono nell else",Toast.LENGTH_SHORT).show();
                            }

                                 */
                                Toast.makeText(view.getContext(), ""+LezID, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }

        public TextView getGiornoID(){return giornoID;}
        public TextView getCapitoloID(){return capitoloID;}
        public TextView getOrarioID(){return orarioID;}
        public TextView getMeseID(){return meseID;}
        public TextView getAnnoID(){return annoID;}

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
        }

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

    }


    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<MyEvent> lessonList) {
        this.lessons = lessonList;
    }

}