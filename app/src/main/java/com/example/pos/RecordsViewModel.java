package com.example.pos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecordsViewModel extends ViewModel {
    private MutableLiveData<String> mode = new MutableLiveData<>();

    public void setMode(String mode) {
        this.mode.setValue(mode);
    }

    public LiveData<String> getMode() {
        return mode;
    }
}
