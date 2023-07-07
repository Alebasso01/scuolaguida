package com.example.scuolaguida.ui.prenotazioni;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.MainActivity;
import com.example.scuolaguida.adapter.EventListAdapter;
import com.example.scuolaguida.databinding.FragmentPrenotazioniBinding;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PrenotazioniFragment extends Fragment {

    TextView giorno;
    TextView capitolo;
    TextView orario;

    MyEvent event;

    Button bottone_prenotazione;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("AllLessons");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrenotazioniViewModel prenotazioniViewModel =
                new ViewModelProvider(this).get(PrenotazioniViewModel.class);


        View view = inflater.inflate(R.layout.fragment_prenotazioni, container, false);
        View view2 = inflater.inflate(R.layout.lezioni_layout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview);

        /*bottone_prenotazione = view2.findViewById(R.id.bottone_prenotati);
        bottone_prenotazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.getEventID();

            }
        });*/

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        EventListAdapter adapter = new EventListAdapter(new ArrayList<MyEvent>());
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MyEvent> events = new ArrayList<MyEvent>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    MyEvent event = data.getValue(MyEvent.class);
                    System.out.println("Adding new event: " + data.getValue(MyEvent.class).getCapitolo());
                    events.add(event);
                }

                // Aggiorna l'adapter con i nuovi dati
                adapter.setLessons(events);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        return view;
    }

    /*private void findCardViewID(int position){
        if(adapter!=null && position<adapter.getItemCount() && position>=0){
            List<Integer> CardViewsID = adapter.getCardViewsIDs();
            if(CardViewsID != null && position < CardViewsID.size()){
                int cardviewid = CardViewsID.get(position);
                RecyclerView recyclerView = getView().findViewById(R.id.idRecycleview);
                if(recyclerView!=null){
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(cardviewid);
                    if(viewHolder != null){
                        CardView cardView = viewHolder.itemView.findViewById(cardviewid);
                        savegiorno = cardView.findViewById(R.id.textView1);
                        savecapitolo = cardView.findViewById(R.id.textView2);
                        saveorario = cardView.findViewById(R.id.textView3);
                    }
                }
            }
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}