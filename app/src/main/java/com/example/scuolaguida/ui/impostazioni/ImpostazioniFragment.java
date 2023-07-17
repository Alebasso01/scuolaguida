package com.example.scuolaguida.ui.impostazioni;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


        return view;
    }
}
