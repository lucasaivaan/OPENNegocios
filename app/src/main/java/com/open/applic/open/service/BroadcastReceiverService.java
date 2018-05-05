package com.open.applic.open.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ivan on 25/1/2018.
 */

public class BroadcastReceiverService extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context.getApplicationContext(),  ServiseNotify.class);
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(service);

    }
}
