package com.example.scuolaguida.ui.impostazioni;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ImpostazioniViewModel {
    private final MutableLiveData<String> mText;

    public ImpostazioniViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
