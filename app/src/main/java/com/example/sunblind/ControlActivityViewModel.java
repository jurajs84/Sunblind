package com.example.sunblind;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ControlActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> statusInfo;
    private final SunblindRepository repository;
    private final LiveData<List<Sunblind>> allSunblindList;
    private final ButtonOrder buttonOrder;

    public ControlActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new SunblindRepository(application);
        allSunblindList = repository.getSunblindList();
        buttonOrder = new ButtonOrder();
        statusInfo = buttonOrder.getSunblindInfo();
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

    public void allSelectedUpOn(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.upOn(ipAddress);
        }
    }

    public void allSelectedUpOff(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.upOff(ipAddress);
        }
    }

    public void allSelectedDownOn(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.downOn(ipAddress);
        }
    }

    public void allSelectedDownOff(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.downOff(ipAddress);
        }
    }

    public void getStatus(String ip) {
        buttonOrder.getStatusInfo(ip);
    }

    LiveData<String> getStatusInfo() {
        if (statusInfo == null) {
            statusInfo = new MutableLiveData<>();
        }
        return statusInfo;
    }
}
