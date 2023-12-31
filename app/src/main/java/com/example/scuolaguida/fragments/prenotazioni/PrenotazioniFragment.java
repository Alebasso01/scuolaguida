package com.example.scuolaguida.fragments.prenotazioni;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.MainActivity;
import com.example.scuolaguida.adapter.EventListAdapter;
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

public class PrenotazioniFragment extends Fragment {
    private FirebaseWrapper firebaseWrapper;

    public PrenotazioniFragment(){
        firebaseWrapper = new FirebaseWrapper();
    }

    TextView giorno;
    TextView mese;
    TextView anno;
    TextView capitolo;
    TextView orario;
    TextView capienza;
    private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();

    MyEvent event;
    Button bottone_prenotazione;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference().child("AllLessons").child("teoria");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrenotazioniViewModel prenotazioniViewModel =
                new ViewModelProvider(this).get(PrenotazioniViewModel.class);


        View view = inflater.inflate(R.layout.fragment_prenotazioni, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview);
        View view2 = inflater.inflate(R.layout.lezioni_layout, container, false);
        capitolo = view2.findViewById(R.id.capitoloID);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        TextView descrizione = view2.findViewById(R.id.stringadescrizione);
        EventListAdapter adapter = new EventListAdapter(new ArrayList<MyEvent>(), getContext());
        recyclerView.setAdapter(adapter);
        ImageView macchina = view.findViewById(R.id.imageView1);
        ImageView bus = view.findViewById(R.id.imageView3);
        ImageView camion = view.findViewById(R.id.imageView2);
        TextView patenteA = view.findViewById(R.id.textView1);
        TextView patenteD = view.findViewById(R.id.textView3);
        TextView patenteC = view.findViewById(R.id.textView2);

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
                // Resetta i colori delle patenti
                ColorBlack(patenteA);
                ColorBlack(patenteD);
                ColorBlack(patenteC);
                // Inizializza la stringa per il child da recuperare
                String child = "";

                if (view.getId() == R.id.imageView1) {
                    macchina.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteA);
                    child = "macchina";
                } else if (view.getId() == R.id.imageView2) {
                    camion.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteC);
                    child = "camion";
                } else if (view.getId() == R.id.imageView3) {
                    bus.setBackgroundColor(Color.YELLOW);
                    ColorRed(patenteD);
                    child = "bus";
                }

                ref.child(child).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<MyEvent> events = new ArrayList<>();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            MyEvent event = data.getValue(MyEvent.class);
                            int aa = Integer.parseInt(event.getAnno());
                            int mm = Integer.parseInt(event.getMese());
                            int gg = Integer.parseInt(event.getGiorno());
                            String cap = event.getCapitolo();
                            if (aa > year) {
                                events.add(event);
                            } else if (aa == year && mm > month) {
                                events.add(event);
                            } else if (aa == year && mm == month && gg >= day) {
                                events.add(event);
                            }
                        }

                        // Ordina la lista degli eventi
                        Collections.sort(events, new Comparator<MyEvent>() {
                            @Override
                            public int compare(MyEvent event1, MyEvent event2) {
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

        return view;
    }

    public void ColorRed(TextView text){
        text.setTextColor(Color.RED);
        text.setTextSize(25);
    }
    public void ColorBlack(TextView selezione) {
        selezione.setTextColor(Color.BLACK);
        selezione.setTextSize(15);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_home);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}