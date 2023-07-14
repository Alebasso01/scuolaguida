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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.adapter.HomeAdapter;
import com.example.scuolaguida.databinding.FragmentHomeBinding;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.example.scuolaguida.models.MyEvent;
//import com.example.scuolaguida.models.MyUser;
import com.example.scuolaguida.models.MyUser;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

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
    //private List<MyEvent> events;
    private  List<MyUser> users;
    public interface OnButtonClickListener {
        void onButtonClicked();
    }
    DatabaseReference ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("users");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.idRecycleview_home);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        users = new ArrayList<>();
        String uid = auth.getUid();
        DatabaseReference userRef = ref.child(uid);
        Button botton = view.findViewById(R.id.bottonenuovaprenotazione);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MyEvent> events;
                events = new ArrayList<>();
                adapter = new HomeAdapter(events);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot data : snapshot.getChildren()) {
                    String userId = data.getKey();

                    if (userId.equals(uid)) {
                        for (DataSnapshot lessonSnapshot : data.getChildren()) {
                            MyEvent lesson = lessonSnapshot.getValue(MyEvent.class);
                            events.add(lesson);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.nav_prenotazioni);
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