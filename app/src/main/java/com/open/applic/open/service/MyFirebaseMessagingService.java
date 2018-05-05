package com.open.applic.open.service;

import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;

/**
 * Created by ivan on 24/1/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String datos=remoteMessage.getData().get("Objetos");
        Mensaje();
    }

    private void Mensaje(){
        Intent i = new Intent(MainActivity_interface_principal.MENSAJE);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }
}
