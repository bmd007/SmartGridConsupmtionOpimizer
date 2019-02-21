package ir.tiroon.schedulingApp.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.JavaUtil.Util;
import ir.tiroon.schedulingApp.MainActivity;
import ir.tiroon.schedulingApp.Model.UsageRecord;
import schedulingApp.tiroon.ir.R;

import static android.content.ContentValues.TAG;

public class MQTTService extends Service {
    final Context THIS = this;

    final String sendToMobileTopic = "mobile_app_topic";
    MqttClient client;

    MemoryPersistence memoryPersistence = new MemoryPersistence();

    public void onCreate() {

        System.out.println("BMD::OnCreate");
        String clientId = MqttClient.generateClientId();
        try {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);

            client = new MqttClient("tcp://"+Util.base_url+":1883", clientId, memoryPersistence);

                client.connect(mqttConnectOptions);

            client.subscribe(sendToMobileTopic, 2);

            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                }

                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                            messageProcessor(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void messageProcessor(String msg) throws Exception {
        JSONObject message = new JSONObject(msg.toString());

        switch (message.getString("type")) {
            case "rtr": {
                createNotification("Refresh token asked", " so login again");
                LocalSchedulerWebServicesCallUtil.loginStatus = "offline";
                break;
            }
            case "lsr": {
                ObjectMapper om = new ObjectMapper();
                UsageRecord ur = om.readValue(message.getString("usageRecord"), UsageRecord.class);

//                    UsageRecord usageRecord = Select.from(UsageRecord.class).where(Condition.prop("date").eq(ur.getDate())).list().get(0);
                //        UsageRecord.find(UsageRecord.class, "device = ? and date = ?", String.valueOf(device.getId()),
//                String.valueOf(theDay.getTime()));

                UsageRecord usageRecord = UsageRecord.findById(UsageRecord.class, ur.getId());

                if (usageRecord != null) {
                    usageRecord.setScheduledVector(ur.getScheduledVector());
                    usageRecord.save();
                }
                else
                    ur.save();

                createNotification("Scheduled usage Vector", "Scheduled Usage times for " + ur.getDevice().getName() + " is now available");
                break;
            }

        }
    }

    public int onStart(Intent intent, int flags, int startId) {
        System.out.println("BMD::OnStart");
            try {
                client.reconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        return START_REDELIVER_INTENT;
    }


    public void createNotification(String title, String body) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(THIS);
        mBuilder.setSmallIcon(R.drawable.splash);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setAutoCancel(true);


        Intent resultIntent = new Intent(this, MainActivity.class);
        android.app.TaskStackBuilder stackBuilder = android.app.TaskStackBuilder.create(THIS);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        android.app.PendingIntent rePendingIntent = stackBuilder.getPendingIntent(0, android.app.PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(rePendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify("BMD noti.", 111, mBuilder.build());

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                client.reconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }

        System.out.println("BMD::OnStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    public void onDestroy() {
        System.out.println("BMD::OnDestroy");
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

//    we dont implement it because we want client independent service which last until we want in back group
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("BMD::OnBind");
        if (!client.isConnected())
            try {
                client.reconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        return null;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}