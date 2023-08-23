package com.example.scuolaguida.fragments.prenotazioni;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.adapter.EventListAdapter;
import com.example.scuolaguida.adapter.PraticaAdapter;
import com.example.scuolaguida.models.EventPratica;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PraticaFragment extends Fragment {

    private FirebaseWrapper firebaseWrapper;

    public PraticaFragment() {
        firebaseWrapper = new FirebaseWrapper();
    }

    private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference().child("AllLessons").child("pratica");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pratica, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerpratica);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        PraticaAdapter adapter = new PraticaAdapter(new ArrayList<EventPratica>(), getContext());
        recyclerView.setAdapter(adapter);
        ImageView macchina = view.findViewById(R.id.imageview1);
        ImageView bus = view.findViewById(R.id.imageView4);
        ImageView camion = view.findViewById(R.id.imageView3);
        ImageView moto = view.findViewById(R.id.imageView2);
        TextView patenteA = view.findViewById(R.id.textView1);
        TextView patenteD = view.findViewById(R.id.textView4);
        TextView patenteC = view.findViewById(R.id.textView3);
        TextView patenteB = view.findViewById(R.id.textView2);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
        int year = calendar.get(Calendar.YEAR);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Resetta lo sfondo di tutti i bottoni
                macchina.setBackgroundColor(Color.TRANSPARENT);
                bus.setBackgroundColor(Color.TRANSPARENT);
                camion.setBackgroundColor(Color.TRANSPARENT);
                moto.setBackgroundColor(Color.TRANSPARENT);
                // Resetta i colori delle patenti
                ColorBlack(patenteA);
                ColorBlack(patenteD);
                ColorBlack(patenteC);
                ColorBlack(patenteB);
                // Inizializza la stringa per il child da recuperare
                String child = "";

                if (view.getId() == R.id.imageview1) {
                    macchina.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteA);
                    child = "macchina";
                } else if (view.getId() == R.id.imageView2) {
                    moto.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteB);
                    child = "moto";
                } else if (view.getId() == R.id.imageView3) {
                    camion.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteC);
                    child = "camion";
                }
                else if (view.getId() == R.id.imageView4) {
                    bus.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteD);
                    child = "bus";
                }

                ref.child(child).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<EventPratica> events = new ArrayList<>();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            EventPratica event = data.getValue(EventPratica.class);
                            int aa = Integer.parseInt(event.getAnno());
                            int mm = Integer.parseInt(event.getMese());
                            int gg = Integer.parseInt(event.getGiorno());
                            if (aa > year) {
                                events.add(event);
                            } else if (aa == year && mm > month) {
                                events.add(event);
                            } else if (aa == year && mm == month && gg >= day) {
                                events.add(event);
                            }
                        }

                        // Ordina la lista degli eventi
                        Collections.sort(events, new Comparator<EventPratica>() {
                            @Override
                            public int compare(EventPratica event1, EventPratica event2) {
                                // Logica di ordinamento basata sulla data
                                int confrontoAnno = Integer.compare(Integer.parseInt(event1.getAnno()), Integer.parseInt(event2.getAnno()));
                                if (confrontoAnno != 0) {
                                    return confrontoAnno;
                                }

                                int confrontoMese = Integer.compare(Integer.parseInt(event1.getMese()), Integer.parseInt(event2.getMese()));
                                if (confrontoMese != 0) {
                                    return confrontoMese;
                                }

                                return Integer.compare(Integer.parseInt(event1.getGiorno()), Integer.parseInt(event2.getGiorno()));
                            }
                        });

                        // Aggiorna l'adapter con i nuovi dati
                        adapter.setLessons(events);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        };

        macchina.setOnClickListener(buttonClickListener);
        bus.setOnClickListener(buttonClickListener);
        camion.setOnClickListener(buttonClickListener);
        moto.setOnClickListener(buttonClickListener);

        return view;
    }
    public void ColorRed(TextView text){
        text.setTextColor(Color.RED);
        text.setTextSize(22);
    }
    public void ColorBlack(TextView selezione) {
        selezione.setTextColor(Color.BLACK);
        selezione.setTextSize(15);
    }
}