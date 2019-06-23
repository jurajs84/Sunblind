package com.example.sunblind;

import androidx.lifecycle.ViewModel;

public class ControlAllActivityViewModel extends ViewModel {

    private ButtonOrder buttonOrder;

    public ControlAllActivityViewModel() {
        buttonOrder = new ButtonOrder();
    }

    public void allUpOn(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.upOn(ipAddress);
        }
    }

    public void allUpOff(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.upOff(ipAddress);
        }
    }

    public void allDownOn(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.downOn(ipAddress);
        }
    }

    public void allDownOff(String[] ip) {
        for (String ipAddress : ip) {
            buttonOrder.downOff(ipAddress);
        }
    }
}
