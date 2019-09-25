package com.example.sunblind;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SunblindItemViewModel extends ViewModel {

    private MutableLiveData<String> statusInfo;
    private ButtonOrder buttonOrder;

    public SunblindItemViewModel() {
        buttonOrder = new ButtonOrder();
        statusInfo = buttonOrder.getSunblindInfo();
    }

    LiveData<String> getStatusInfo() {
        if (statusInfo == null) {
            statusInfo = new MutableLiveData<>();
        }
        return statusInfo;
    }

    public void upOn(String ip) {
        buttonOrder.upOn(ip);
    }

    public void upOff(String ip) {
        buttonOrder.upOff(ip);
    }

    public void downOn(String ip) {
        buttonOrder.downOn(ip);
    }

    public void downOff(String ip) {
        buttonOrder.downOff(ip);
    }

    public void getStatus(String ip) {
        buttonOrder.getStatusInfo(ip);
    }
}
