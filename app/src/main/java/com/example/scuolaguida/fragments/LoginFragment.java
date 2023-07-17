package com.example.scuolaguida.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.EnterActivity;
import com.example.scuolaguida.models.FirebaseWrapper;

public class LoginFragment extends LogFragment {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initArguments();
    }
    boolean passwordvisibility = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // See: https://developer.android.com/reference/android/view/LayoutInflater#inflate(org.xmlpull.v1.XmlPullParser,%20android.view.ViewGroup,%20boolean)
        View externalView = inflater.inflate(R.layout.fragment_login, container, false);

        TextView link = externalView.findViewById(R.id.switchLoginToRegisterLabel);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EnterActivity) LoginFragment.this.requireActivity()).renderFragment(false);
            }
        });



        Button button = externalView.findViewById(R.id.logButton);
        EditText email = externalView.findViewById(R.id.userEmail);
        email.setInputType(InputType.TYPE_CLASS_TEXT);
        ImageView passwordVisibilityToggle = externalView.findViewById(R.id.passwordVisibilityToggle);
        EditText passwordEditText = externalView.findViewById(R.id.userPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()){
                    email.setError("Email is required");
                }
                else if(passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("Password is required");
                }
                else {

                    // Perform SignIn
                    FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
                    auth.signIn(
                            email.getText().toString(),
                            passwordEditText.getText().toString(),
                            FirebaseWrapper.Callback
                                    .newInstance(LoginFragment.this.requireActivity(),
                                            LoginFragment.this.callbackName,
                                            LoginFragment.this.callbackPrms)

                    );
                }
            }
        });
        passwordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordvisibility) {
                    passwordvisibility = false;
                    passwordVisibilityToggle.setImageResource(R.drawable.baseline_visibility_off_24);
                    passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    passwordvisibility = true;
                    passwordVisibilityToggle.setImageResource(R.drawable.baseline_visibility_24);
                    passwordEditText.setTransformationMethod(null);
                }

                // Imposta il cursore alla fine del testo
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });


        return externalView;
    }
}


