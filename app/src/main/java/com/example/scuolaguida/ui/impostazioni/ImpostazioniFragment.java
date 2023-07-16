package com.example.scuolaguida.ui.impostazioni;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scuolaguida.R;

public class ImpostazioniFragment extends Fragment {

    private ImpostazioniViewModel mViewModel;

    public static ImpostazioniFragment newInstance() {
        return new ImpostazioniFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_impostazioni, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImpostazioniViewModel.class);
        // TODO: Use the ViewModel
    }

}