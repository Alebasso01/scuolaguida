package com.example.scuolaguida.fragments;

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
import android.widget.Toast;

import com.example.scuolaguida.R;
import com.example.scuolaguida.activities.EnterActivity;
import com.example.scuolaguida.models.FirebaseWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends LogFragment {

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initArguments();
    }
    boolean passwordvisibility = false;
    boolean isPasswordvisibility2 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // See: https://developer.android.com/reference/android/view/LayoutInflater#inflate(org.xmlpull.v1.XmlPullParser,%20android.view.ViewGroup,%20boolean)
        View externalView = inflater.inflate(R.layout.fragment_signup, container, false);

        TextView link = externalView.findViewById(R.id.switchRegisterToLoginLabel);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EnterActivity) SignupFragment.this.requireActivity()).renderFragment(true);
            }
        });
        Button button = externalView.findViewById(R.id.signupbutton);
        EditText email = externalView.findViewById(R.id.userEmail);
        email.setInputType(InputType.TYPE_CLASS_TEXT);
        ImageView passwordVisibilityToggle = externalView.findViewById(R.id.passwordVisibilityToggle);
        EditText passwordEditText = externalView.findViewById(R.id.userPassword);
        EditText passwordagain = externalView.findViewById(R.id.userPasswordAgain);
        ImageView passwordvisibility2 = externalView.findViewById(R.id.passwordVisibilityToggle2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()){
                    email.setError("Email is required");
                }
                if(passwordEditText.getText().toString().isEmpty()){
                    passwordEditText.setError("Password is required");
                }
                if(passwordagain.getText().toString().isEmpty()){
                    passwordagain.setError("Password is required");
                }
                else if (!passwordEditText.getText().toString().equals(passwordagain.getText().toString())) {
                    // TODO: Better error handling + remove this hardcoded strings
                    Toast
                            .makeText(SignupFragment.this.requireActivity(), "Passwords are different", Toast.LENGTH_LONG)
                            .show();
                }
                else if (!hasSpecialCharacter(passwordEditText.getText().toString())) {
                    passwordEditText.setError("Password must contain at least one special character and one uppercase letter");
                }
                else {

                    // Perform SignIn
                    FirebaseWrapper.Auth auth = new FirebaseWrapper.Auth();
                    auth.signUp(
                            email.getText().toString(),
                            passwordEditText.getText().toString(),
                            FirebaseWrapper.Callback
                                    .newInstance(SignupFragment.this.requireActivity(),
                                            SignupFragment.this.callbackName,
                                            SignupFragment.this.callbackPrms)
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

        passwordvisibility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordvisibility2) {
                    isPasswordvisibility2 = false;
                    passwordvisibility2.setImageResource(R.drawable.baseline_visibility_off_24);
                    passwordagain.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    isPasswordvisibility2 = true;
                    passwordvisibility2.setImageResource(R.drawable.baseline_visibility_24);
                    passwordagain.setTransformationMethod(null);
                }

                // Imposta il cursore alla fine del testo
                passwordagain.setSelection(passwordagain.getText().length());
            }
        });

        return externalView;
    }
    private boolean hasSpecialCharacter(String password) {
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        return specialCharPattern.matcher(password).find() && upperCasePattern.matcher(password).find();
    }

}