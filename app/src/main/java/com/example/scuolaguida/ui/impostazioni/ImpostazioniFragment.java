package com.example.scuolaguida.ui.impostazioni;
import android.app.LocaleManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.scuolaguida.R;

import java.util.Locale;

public class ImpostazioniFragment extends Fragment {

    public ImpostazioniFragment() {
        // Costruttore vuoto richiesto
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla il layout per il Fragment delle impostazioni
        View view = inflater.inflate(R.layout.fragment_impostazioni, container, false);

        Spinner cambiolingua = view.findViewById(R.id.cambiolingua);
        cambiolingua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Lingua selezionata: " + selectedLanguage, Toast.LENGTH_SHORT).show();

                // Esegui le azioni desiderate in base alla lingua selezionata
                if (selectedLanguage.equals(getString(R.string.Italiano))) {
                    Locale locale = new Locale("it-rIT");
                    Configuration configuration = getResources().getConfiguration();
                    configuration.setLocale(locale);
                    getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                } else if (selectedLanguage.equals(getString(R.string.Inglese))){
                    Locale locale = new Locale("en");
                    Configuration configuration = getResources().getConfiguration();
                    configuration.setLocale(locale);
                    getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
                else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nessuna selezione
            }
        });

       /* cambiolingua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //getActivity().recreate();
                Toast.makeText(getActivity(), "tema cambiato", Toast.LENGTH_SHORT).show();
                Locale locale = new Locale("en");
                Configuration configuration = getResources().getConfiguration();
                configuration.setLocale(locale);
                getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                //getActivity().setTheme(R.style.Theme_Scuolaguida_night);
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });*/

        return view;
    }
}
