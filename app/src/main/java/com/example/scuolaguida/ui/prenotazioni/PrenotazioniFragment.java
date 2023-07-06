package com.example.scuolaguida.ui.prenotazioni;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
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

import java.util.ArrayList;
import java.util.List;

public class PrenotazioniFragment extends Fragment {
    //List<MyEvent> events = new ArrayList<MyEvent>();
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("AllLessons");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrenotazioniViewModel prenotazioniViewModel =
                new ViewModelProvider(this).get(PrenotazioniViewModel.class);

        View view = inflater.inflate(R.layout.fragment_prenotazioni, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview);

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}