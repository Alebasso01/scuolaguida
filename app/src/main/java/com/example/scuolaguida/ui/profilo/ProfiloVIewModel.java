package com.example.scuolaguida.ui.profilo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfiloVIewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfiloVIewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}