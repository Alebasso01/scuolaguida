package com.example.scuolaguida.fragments.prenotazioni;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PraticaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PraticaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}