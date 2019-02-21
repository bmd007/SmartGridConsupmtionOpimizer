package ir.tiroon.schedulingApp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.orm.SugarContext;
import com.orm.util.SugarConfig;

import ir.tiroon.schedulingApp.Model.Device;
import ir.tiroon.schedulingApp.Model.UsageRecord;
import ir.tiroon.schedulingApp.Services.MQTTService;
import schedulingApp.tiroon.ir.R;

public class SplashScreen extends AppCompatActivity {
    final int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);

        SugarConfig.clearCache();
        SugarContext.init(getApplicationContext());

        Device device = new Device((long) 10200, "bmd", "avc", true, 1000.1);
        device.save();

        UsageRecord usageRecord = new UsageRecord(new java.sql.Date(1000), device);
        usageRecord.save();

        usageRecord.delete();
        device.delete();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                // close this activity

                finish();

            }
        }, SPLASH_TIME_OUT);
    }


}
