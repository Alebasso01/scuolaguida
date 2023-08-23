package com.example.scuolaguida.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.adapter.HomeAdapter;
import com.example.scuolaguida.adapter.PraticaAdapter;
import com.example.scuolaguida.adapter.PraticaHomeAdapter;
import com.example.scuolaguida.databinding.FragmentHomeBinding;
import com.example.scuolaguida.models.EventPratica;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.models.MyUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
    private HomeAdapter homeAdapter;
    private PraticaHomeAdapter praticaHomeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(new ArrayList<>());
        praticaHomeAdapter = new PraticaHomeAdapter(new ArrayList<>());

        ConcatAdapter concatAdapter = new ConcatAdapter(homeAdapter, praticaHomeAdapter);
        recyclerView.setAdapter(concatAdapter);


        DatabaseReference userRef = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("users").child(auth.getUid());

        userRef.child("teoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MyEvent> events = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MyEvent lesson = data.getValue(MyEvent.class);
                    events.add(lesson);
                }
                Collections.sort(events, new Comparator<MyEvent>() {
                    @Override
                    public int compare(MyEvent event1, MyEvent event2) {
                        // Logica di ordinamento basata sulla data
                        if (event1.getAnno() != null && event2.getAnno() != null) {
                            int confrontoAnno = Integer.compare(Integer.parseInt(event1.getAnno()), Integer.parseInt(event2.getAnno()));
                            if (confrontoAnno != 0) {
                                return confrontoAnno;
                            }
                        }

                        if (event1.getMese() != null && event2.getMese() != null) {
                            int confrontoMese = Integer.compare(Integer.parseInt(event1.getMese()), Integer.parseInt(event2.getMese()));
                            if (confrontoMese != 0) {
                                return confrontoMese;
                            }
                        }
                        if (event1.getGiorno() != null && event2.getGiorno() != null) {
                            int confrontogiorno = Integer.compare(Integer.parseInt(event1.getGiorno()), Integer.parseInt(event2.getGiorno()));
                            if (confrontogiorno != 0) {
                                return confrontogiorno;
                            }
                        }
                        String[] orario1 = event1.getOrario() != null ? event1.getOrario().split(":") : new String[0];
                        String[] orario2 = event2.getOrario() != null ? event2.getOrario().split(":") : new String[0];

                        int confrontoOra = 0;

                        if (orario1.length > 0 && orario2.length > 0) {
                            int ora1 = Integer.parseInt(orario1[0]);
                            int ora2 = Integer.parseInt(orario2[0]);
                            confrontoOra = Integer.compare(ora1, ora2);
                        }

                        return confrontoOra;
                    }
                });
                homeAdapter.setLessons(events);
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userRef.child("pratica").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EventPratica> events2 = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    EventPratica lesson = data.getValue(EventPratica.class);
                    events2.add(lesson);
                }
                Collections.sort(events2, new Comparator<EventPratica>() {
                    @Override
                    public int compare(EventPratica event1, EventPratica event2) {
                        // Logica di ordinamento basata sulla data
                        if (event1.getAnno() != null && event2.getAnno() != null) {
                            int confrontoAnno = Integer.compare(Integer.parseInt(event1.getAnno()), Integer.parseInt(event2.getAnno()));
                            if (confrontoAnno != 0) {
                                return confrontoAnno;
                            }
                        }

                        if (event1.getMese() != null && event2.getMese() != null) {
                            int confrontoMese = Integer.compare(Integer.parseInt(event1.getMese()), Integer.parseInt(event2.getMese()));
                            if (confrontoMese != 0) {
                                return confrontoMese;
                            }
                        }
                        if (event1.getGiorno() != null && event2.getGiorno() != null) {
                            int confrontogiorno = Integer.compare(Integer.parseInt(event1.getGiorno()), Integer.parseInt(event2.getGiorno()));
                            if (confrontogiorno != 0) {
                                return confrontogiorno;
                            }
                        }
                        String[] orario1 = event1.getOrario() != null ? event1.getOrario().split(":") : new String[0];
                        String[] orario2 = event2.getOrario() != null ? event2.getOrario().split(":") : new String[0];

                        int confrontoOra = 0;

                        if (orario1.length > 0 && orario2.length > 0) {
                            int ora1 = Integer.parseInt(orario1[0]);
                            int ora2 = Integer.parseInt(orario2[0]);
                            confrontoOra = Integer.compare(ora1, ora2);
                        }

                        return confrontoOra;
                    }
                });
                praticaHomeAdapter.setLessons(events2);
                praticaHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button botton = view.findViewById(R.id.bottonenuovaprenotazione);
        Button bottonpratica = view.findViewById(R.id.bottoneprenotazionepratica);

        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.nav_prenotazioni);
            }
        });

        bottonpratica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.nav_pratica);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
