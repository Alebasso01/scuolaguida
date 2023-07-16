package com.example.scuolaguida.ui.profilo;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.EnterActivity;
import com.example.scuolaguida.fragments.LoginFragment;
import com.example.scuolaguida.models.CalendarProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ProfiloFragment extends Fragment {
    private void goToActivity(Class<?> activity) {
        Intent intent = new Intent(getActivity(), activity);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        TextView emailtextview = view.findViewById(R.id.emailTextView);
        emailtextview.setText(user.getEmail());
        TextView logout = view.findViewById(R.id.logout);
        Button bottoneelimina = view.findViewById(R.id.bottoneelimina);
        TextView cambiapassw = view.findViewById(R.id.cambiapassw);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                goToActivity(EnterActivity.class);
            }
        });
        String emailAddress = "didonna.emiliani@gmail.com";

        cambiapassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "email mandata", Toast.LENGTH_SHORT).show();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
            }
        });

        bottoneelimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.delete();
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}