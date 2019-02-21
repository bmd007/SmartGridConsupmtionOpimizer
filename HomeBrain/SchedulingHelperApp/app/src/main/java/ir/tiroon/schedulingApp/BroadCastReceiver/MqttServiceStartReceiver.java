package ir.tiroon.schedulingApp.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.tiroon.schedulingApp.Services.MQTTService;

/**
 * Created by Lenovo on 11/11/2017.
 */
public class MqttServiceStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MQTTService.class));
    }
}
