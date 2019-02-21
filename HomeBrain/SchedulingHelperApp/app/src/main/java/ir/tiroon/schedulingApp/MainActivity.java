package ir.tiroon.schedulingApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import ir.tiroon.schedulingApp.CustomLayout.MyDrawer;
import ir.tiroon.schedulingApp.Dialog.LoginDialog;
import ir.tiroon.schedulingApp.Dialog.NewDeviceDialog;
import ir.tiroon.schedulingApp.Services.MQTTService;
import ir.tiroon.schedulingApp.Util.Languages;
import ir.tiroon.schedulingApp.Util.MyUtil;
import schedulingApp.tiroon.ir.R;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    Activity THIS = this;

    public static MyDrawer myDrawer;
    public static TextToSpeech myTTS;

    public MyUtil myUtil;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myDrawer.selectAMenu(MyUtil.LastSelectedFragmentNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--------------------------------
        if (!MyUtil.isServiceRunning(this))
            startService(new Intent(getApplicationContext(), MQTTService.class));
        //------------------

        myUtil = new MyUtil(this, getBaseContext());
//        myUtil.changeLanguage();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c63f43")));

//        getWindow().getDecorView().setLayoutDirection(MyUtil.languageSelector == Languages.persian ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);

        myDrawer = new MyDrawer(this);
        myDrawer.selectAMenu(MyUtil.LastSelectedFragmentNumber);
//        ----------------------------------------
        FloatingActionButton addDeviceB = (FloatingActionButton) findViewById(R.id.addDeviceFButton);
        addDeviceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewDeviceDialog ndd = new NewDeviceDialog(THIS);
                ndd.show();
            }
        });

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = myDrawer.getmDrawerLayout().isDrawerOpen(myDrawer.getmDrawerList());
//        menu.findItem(R.id.action_drawer).setVisible(!drawerOpen);
//        menu.findItem(R.id.refresh_menu).setVisible(!drawerOpen);

        MenuItem pmi = menu.findItem(R.id.language_menu_persian);

        MenuItem emi = menu.findItem(R.id.language_menu_english);
//
//        if (MyUtil.languageSelector == Languages.english) {
//            pmi.setVisible(true);
//            emi.setVisible(false);
//        } else {
//            pmi.setVisible(false);
//            emi.setVisible(true);
//        }


        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_drawer) {
            if (myDrawer.getmDrawerToggle().onOptionsItemSelected(item))
                return true;
            return false;
        } else if (id == R.id.login_menu) {
            new LoginDialog(this).show();
            return true;
        } else if (id == R.id.language_menu_persian) {

            MyUtil.languageSelector = Languages.persian;
            myUtil.changeLanguage();

            restartActivity();
            return true;

        } else if (id == R.id.language_menu_english) {

            MyUtil.languageSelector = Languages.english;
            myUtil.changeLanguage();

            restartActivity();
            return true;

        } else if (id == R.id.speak_permission_menu) {

            MyUtil.isAllowedToSpeak = !MyUtil.isAllowedToSpeak;
            item.setIcon(MyUtil.isAllowedToSpeak ? R.drawable.speak : R.drawable.mute);

        }

        return super.onOptionsItemSelected(item);
    }


    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        myDrawer.getmDrawerToggle().syncState();
    }
}
