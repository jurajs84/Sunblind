package com.example.sunblide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SunblideItemViewModel extends ViewModel {

    private MutableLiveData<String> statusInfo;
    private ButtonOrder buttonOrder;

    public SunblideItemViewModel() {
        buttonOrder = new ButtonOrder();
        statusInfo = buttonOrder.getSunblideInfo();
    }

    LiveData<String> getStatusInfo() {
        if (statusInfo == null) {
            statusInfo = new MutableLiveData<>();
//            upOn();
//            upOff();
        }
        return statusInfo;
    }

    public void upOn(String ip) {
        buttonOrder.upOn(ip);
        //statusInfo.setValue(ipInfo);
    }

    public void upOff(String ip) {
        buttonOrder.upOff(ip);
        //statusInfo.setValue(ipInfo);
    }
}
