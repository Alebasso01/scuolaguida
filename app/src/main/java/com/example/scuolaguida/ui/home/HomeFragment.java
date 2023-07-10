package com.example.scuolaguida.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.adapter.EventListAdapter;
import com.example.scuolaguida.adapter.HomeAdapter;
import com.example.scuolaguida.databinding.FragmentHomeBinding;
import com.example.scuolaguida.models.CalendarProvider;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
import com.example.scuolaguida.models.MyUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TextView giorno;
    TextView mese;
    TextView anno;
    TextView capitolo;
    TextView orario;
    private FirebaseWrapper firebaseWrapper;
    private FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
    public HomeFragment(){
        firebaseWrapper = new FirebaseWrapper();
    }
    private HomeAdapter adapter;
    private List<MyEvent> events;
    private  List<MyUser> users;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("users");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview_home);
        View view2 = inflater.inflate(R.layout.lezioni_home, container, false);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        events = new ArrayList<>();
        users = new ArrayList<>();
        adapter = new HomeAdapter(events);
        recyclerView.setAdapter(adapter);
        String uid = auth.getUid();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    String userId = data.getKey();
                    //Toast.makeText(view.getContext(), "id:" +userId, Toast.LENGTH_SHORT).show();
                    if (userId.equals(uid)) {
                        for (DataSnapshot lessonSnapshot : data.getChildren()) {
                            MyEvent lesson = lessonSnapshot.getValue(MyEvent.class);
                            MyUser utenti = lessonSnapshot.getValue(MyUser.class);
                            events.add(lesson);
                            users.add(utenti);
                        }
                    }
                }

                adapter.notifyDataSetChanged(); // Notifica all'adattatore che i dati sono cambiati
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'errore
            }
        });



        return view;
    }
/*
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2023);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH,8);
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        long starttime = calendar.getTimeInMillis();
        long endtime = starttime+(60*60*1000);

        Button bottonecalendario = view.findViewById(R.id.bottonecalendario);

        bottonecalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarProvider calendarProvider = new CalendarProvider(getActivity().getContentResolver());
                //calendarProvider.writeEventToCalendar(getContext(),"ciao","ciao",starttime,endtime);
                boolean isEventAdded = calendarProvider.writeEventToCalendar(getActivity(),"nuovo evento",
                        "ciao",starttime,endtime);
                if(isEventAdded){
                    Toast.makeText(getActivity(),"evento aggiunto",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"errore",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}