package com.example.sunblide;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SunblideItemViewModel extends ViewModel {

    private MutableLiveData<String> pingInfo;
    private ButtonOrder buttonOrder;

    public SunblideItemViewModel() {
        buttonOrder = new ButtonOrder();
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
        String ipInfo = buttonOrder.upOn(ip);
        //pingInfo.setValue(ipInfo);
    }

    public void upOff(String ip) {
        String ipInfo = buttonOrder.upOff(ip);
        //pingInfo.setValue(ipInfo);
    }
}
