package ir.tiroon.schedulingApp.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

import ir.tiroon.schedulingApp.Services.MQTTService;

/**
 * Created by Lenovo on 20/04/2017.
 */

public class MyUtil {

    public Activity context;

    public static Typeface fonts[] = new Typeface[5];

    public static Languages languageSelector = Languages.persian;

    public static boolean isAllowedToSpeak = true;

    public static int LastSelectedFragmentNumber = 1;

    Context baseContext;

    public MyUtil(Activity context, Context baseContext) {

        this.context = context;

        this.baseContext = baseContext;

        fonts[0] = Typeface.createFromAsset(context.getAssets(), "DIODRUMARABIC-BOLD.TTF");
        fonts[1] = Typeface.createFromAsset(context.getAssets(), "DIODRUMARABIC-EXTRALIGHT.TTF");
        fonts[2] = Typeface.createFromAsset(context.getAssets(), "DIODRUMARABIC-LIGHT.TTF");
        fonts[3] = Typeface.createFromAsset(context.getAssets(), "DIODRUMARABIC-MEDIUM.TTF");
        fonts[4] = Typeface.createFromAsset(context.getAssets(), "DIODRUMARABIC-SEMIBOLD.TTF");

    }

    public void changeLanguage(){

        Locale  locale = null;

        if (languageSelector == Languages.persian)
            locale = new Locale("fa");
        else
            locale = new Locale("en");

        Locale.setDefault(locale);

//        Resources resources = context.createConfigurationContext(baseContext.getResources().getConfiguration()).getResources();
        Resources resources = context.getResources();

        Configuration config = resources.getConfiguration();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
//            config.setLocale(locale);
        }else
            config.locale = locale;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                config.setLayoutDirection(config.getLocales().get(0));
            else
                config.setLayoutDirection(config.locale);



        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }


//    void showSyncDialog() {
//        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String serverUrl = sharedpreferences.getString("serverUrl", "");
//
//        SharedPreferences sharedpreferences1 = PreferenceManager.getDefaultSharedPreferences(THIS);
//        SharedPreferences.Editor editor = sharedpreferences1.edit();
//        editor.remove("serverUrl");
//
//        editor.putString("serverUrl", "http");
//        editor.apply();
//    }

    public static boolean isServiceRunning(Context context){

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            System.out.println("BMD::service::"+service.service.getClassName());
            if (service.service.getClassName().equals("ir.tiroon.schedulingApp.Services.MQTTService"))
                return true;
        }
        return false;

    }

}
