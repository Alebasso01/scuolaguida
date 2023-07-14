package com.example.scuolaguida.fragments.prenotazioni;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
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

    MyEvent event;
    Button bottone_prenotazione;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("AllLessons");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrenotazioniViewModel prenotazioniViewModel =
                new ViewModelProvider(this).get(PrenotazioniViewModel.class);


        View view = inflater.inflate(R.layout.fragment_prenotazioni, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview);
        View view2 = inflater.inflate(R.layout.lezioni_layout, container, false);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        EventListAdapter adapter = new EventListAdapter(new ArrayList<MyEvent>());
        recyclerView.setAdapter(adapter);
        ImageView macchina = view.findViewById(R.id.imageView1);
        ImageView moto = view.findViewById(R.id.imageView2);
        ImageView camion = view.findViewById(R.id.imageView3);
        TextView patenteA = view.findViewById(R.id.textView1);
        TextView patenteB = view.findViewById(R.id.textView2);
        TextView patenteC = view.findViewById(R.id.textView3);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Mese inizia da 0, quindi aggiungi 1
        int year = calendar.get(Calendar.YEAR);

        macchina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                macchina.setBackgroundColor(Color.YELLOW);
                moto.setBackgroundColor(Color.TRANSPARENT);
                camion.setBackgroundColor(Color.TRANSPARENT);
                ColorRed(patenteA);
                ColorBlack(patenteB);
                ColorBlack(patenteC);
                ref.child("macchina").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<MyEvent> events = new ArrayList<MyEvent>();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            MyEvent event = data.getValue(MyEvent.class);
                            int aa = Integer.parseInt(event.getAnno());
                            int mm = Integer.parseInt(event.getMese());
                            int gg = Integer.parseInt(event.getGiorno());
                            if(aa>year) {
                                events.add(event);
                            }
                            else if (aa>=year && mm>month) {
                                events.add(event);
                            }
                            else if(aa>=year && mm>=month && gg>=day){
                                events.add(event);
                            }
                            else{}

                        }

                        // Aggiorna l'adapter con i nuovi dati
                        adapter.setLessons(events);
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        moto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moto.setBackgroundColor(Color.YELLOW);
                macchina.setBackgroundColor(Color.TRANSPARENT);
                camion.setBackgroundColor(Color.TRANSPARENT);
                ColorRed(patenteB);
                ColorBlack(patenteA);
                ColorBlack(patenteC);
                ref.child("moto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<MyEvent> events = new ArrayList<MyEvent>();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            MyEvent event = data.getValue(MyEvent.class);
                            int aa = Integer.parseInt(event.getAnno());
                            int mm = Integer.parseInt(event.getMese());
                            int gg = Integer.parseInt(event.getGiorno());
                            if(aa>year) {
                                events.add(event);
                            }
                            else if (aa==year && mm>month) {
                                events.add(event);
                            }
                            else if(aa==year && mm==month && gg>=day){
                                events.add(event);
                            }
                            else{}
                        }

                        // Aggiorna l'adapter con i nuovi dati
                        adapter.setLessons(events);
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camion.setBackgroundColor(Color.YELLOW);
                moto.setBackgroundColor(Color.TRANSPARENT);
                macchina.setBackgroundColor(Color.TRANSPARENT);
                ColorRed(patenteC);
                ColorBlack(patenteA);
                ColorBlack(patenteB);
                ref.child("moto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<MyEvent> events = new ArrayList<MyEvent>();

                        for (DataSnapshot data : snapshot.getChildren()) {

                            MyEvent event = data.getValue(MyEvent.class);
                            int aa = Integer.parseInt(event.getAnno());
                            int mm = Integer.parseInt(event.getMese());
                            int gg = Integer.parseInt(event.getGiorno());
                            if(aa>year) {
                                events.add(event);
                            }
                            else if (aa==year && mm>month) {
                                events.add(event);
                            }
                            else if(aa==year && mm==month && gg>=day){
                                events.add(event);
                            }
                            else{}
                        }

                        // Aggiorna l'adapter con i nuovi dati
                        adapter.setLessons(events);
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MyEvent> events = new ArrayList<MyEvent>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    MyEvent event = data.getValue(MyEvent.class);
                    events.add(event);
                }

                // Aggiorna l'adapter con i nuovi dati
                adapter.setLessons(events);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

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