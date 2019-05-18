package com.example.sunblide;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class SunblideViewModel extends AndroidViewModel {

    private MutableLiveData<String> pingInfo;
    private SunblideRepository repository;
    private LiveData<List<Sunblide>> allSunblideList;
    private ButtonOrder buttonOrder;

    public SunblideViewModel(Application application) {
        super(application);
        repository = new SunblideRepository(application);
        allSunblideList = repository.getSunblideList();
        buttonOrder = new ButtonOrder();
    }

    public void insert(Sunblide sunblide) {
        repository.insert(sunblide);
    }

    public void update(Sunblide sunblide) {
        repository.update(sunblide);
    }

    public void delete(Sunblide sunblide) {
        repository.delete(sunblide);
    }

    public LiveData<List<Sunblide>> getAllSunblideList() {
        return allSunblideList;
    }

    LiveData<String> getPingInfo() {
        if (pingInfo == null) {
            pingInfo = new MutableLiveData<>();
//            upOn();
//            upOff();
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
