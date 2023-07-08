package com.example.scuolaguida.ui.profilo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.EnterActivity;
import com.example.scuolaguida.fragments.LoginFragment;
import com.example.scuolaguida.models.CalendarProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class ProfiloFragment extends Fragment {
    private void goToActivity(Class<?> activity) {
        Intent intent = new Intent(getActivity(), activity);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
    private Button button;
    private FirebaseAuth auth;
    FirebaseUser user;

    TextView emailtextview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        emailtextview = view.findViewById(R.id.emailTextView);
        emailtextview.setText(user.getEmail());
        button = view.findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                goToActivity(EnterActivity.class);
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}