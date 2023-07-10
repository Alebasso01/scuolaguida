package com.example.scuolaguida.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<MyEvent> lessons;

    public HomeAdapter(List<MyEvent> L) {
        this.lessons = L;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView giornoID;
        private final TextView meseID;
        private final TextView annoID;
        private final TextView orarioID;
        private final TextView capitoloID;
        private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
        CardView cardView = itemView.findViewById(R.id.cardview_home);

        public ViewHolder(View view) {
            super(view);
            this.giornoID = (TextView) view.findViewById(R.id.giornoID);
            this.capitoloID = (TextView) view.findViewById(R.id.capitoloID);
            this.orarioID = (TextView) view.findViewById(R.id.orarioID);
            this.meseID = (TextView) view.findViewById(R.id.meseID);
            this.annoID = (TextView) view.findViewById(R.id.annoID);
            Button bottoneannulla = view.findViewById(R.id.bottone_ANNULLA);

            bottoneannulla.setOnClickListener(new View.OnClickListener() {
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
                    userRef.removeValue();
                }
            });
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
