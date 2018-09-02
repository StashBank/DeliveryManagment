package com.stashbank.deliveryManagement;

import android.app.Application;
import ua.pbank.dio.minipos.MiniPosManager;

public class DeliveryManagementApp extends Application {

    public static DeliveryManagementApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MiniPosManager.builder(this,"123242hasgcfghaf")
                .setEnableLogging(true)
                .build();
        instance = this;
    }
}
