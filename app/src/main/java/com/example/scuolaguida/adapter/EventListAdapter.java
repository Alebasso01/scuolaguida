package com.example.scuolaguida.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.ui.prenotazioni.PrenotazioniFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<MyEvent> lessons;

    public EventListAdapter(List<MyEvent> L){
        this.lessons = L;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView giornoID;
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
            bottoneprenotazioni = view.findViewById(R.id.bottone_prenotati);
                bottoneprenotazioni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String giorno = getGiornoID().getText().toString();
                        String orario = getOrarioID().getText().toString();
                        String capitolo = getCapitoloID().getText().toString();

                        String userId = auth.getUid();
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference();

                        DatabaseReference userRef = databaseRef.child("users").child(userId).push();
                        userRef.child("giorno").setValue(giorno);
                        userRef.child("capitolo").setValue(capitolo);
                        userRef.child("orario").setValue(orario);

                        Toast.makeText(view.getContext(), "Giorno: " + giorno +
                                ", Orario: " + orario + ", Capitolo: " + capitolo, Toast.LENGTH_SHORT).show();
                    }
                });
        }

        public TextView getGiornoID(){return giornoID;}
        public TextView getCapitoloID(){return capitoloID;}
        public TextView getOrarioID(){return orarioID;}

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
    }


    @Override
    public int getItemCount() {return this.lessons.size();}


    public void setLessons(List<MyEvent> lessonList) {
        this.lessons = lessonList;
    }

}