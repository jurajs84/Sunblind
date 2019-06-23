package com.example.sunblind;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import java.util.List;

public class SunblindViewModel extends AndroidViewModel {

    private MutableLiveData<String> pingInfo;
    private SunblindRepository repository;
    private LiveData<List<Sunblind>> allSunblindList;
    private ButtonOrder buttonOrder;

    public SunblindViewModel(Application application) {
        super(application);
        repository = new SunblindRepository(application);
        allSunblindList = repository.getSunblindList();
        buttonOrder = new ButtonOrder();
    }

    public void insert(Sunblind sunblind) {
        repository.insert(sunblind);
    }

    public void update(Sunblind sunblind) {
        repository.update(sunblind);
    }

    public void delete(Sunblind sunblind) {
        repository.delete(sunblind);
    }

    public LiveData<List<Sunblind>> getAllSunblindList() {
        return allSunblindList;
    }

    LiveData<String> getPingInfo() {
        if (pingInfo == null) {
            pingInfo = new MutableLiveData<>();
        }
        return pingInfo;
    }

    public void upOn(String ip) {
        buttonOrder.upOn(ip);
    }

    public void upOff(String ip) {
        buttonOrder.upOff(ip);
    }
}
