package com.example.scuolaguida.ui.profilo;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
        TextView bottoneelimina = view.findViewById(R.id.bottonelimina);
        Context context = requireContext();
        TextView cambiopassword = view.findViewById(R.id.cambiapassw);

        cambiopassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailaddress = emailtextview.getText().toString();
                auth.sendPasswordResetEmail(emailaddress);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(view.getContext().getString(R.string.titolo_cambiopassword));
                builder.setMessage(view.getContext().getString(R.string.contenuto_cambiopassword));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(view.getContext().getString(R.string.titolo_logout));
                builder.setMessage(view.getContext().getString(R.string.contenuto_logout));
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        goToActivity(EnterActivity.class);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        bottoneelimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(view.getContext().getString(R.string.titolo_eliminaaccount));
                builder.setMessage(view.getContext().getString(R.string.contenuto_eliminaccount));
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.delete();
                        if (context != null) {
                            Intent intent = new Intent(context, EnterActivity.class);
                            startActivity(intent);
                            Toast.makeText(context, "utente eliminato correttamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}