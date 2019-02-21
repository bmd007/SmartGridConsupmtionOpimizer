package ir.tiroon.schedulingApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ir.tiroon.schedulingApp.Dialog.UsageRecordShowDialog;
import ir.tiroon.schedulingApp.Fragment.DeviceListFragment;
import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.JavaUtil.Util;
import ir.tiroon.schedulingApp.Model.Device;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import schedulingApp.tiroon.ir.R;

public class DeviceShowActivity extends AppCompatActivity {


    @BindView(R.id.mac_layout)
    LinearLayout macLayout;

    @BindView(R.id.macAddress)
    TextView mac;
    @BindView(R.id.deviceName)
    TextView name;
    @BindView(R.id.usagePerHour)
    TextView usagePerHour;
    @BindView(R.id.isAutomaticTextViewShow)
    TextView isAutomatic;

    @BindViews({R.id.firstDayOfWeekButton, R.id.secondDayOfWeekButton, R.id.thirdDayOfWeekButton,
            R.id.fourthDayOfWeekButton, R.id.fifthDayOfWeekButton, R.id.sixthDayOfWeekButton, R.id.seventhDayOfWeekButton})
    List<Button> weekDaysButtons;

    Device device;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deviceshow);

        ButterKnife.bind(this);

        device = Device.findById(Device.class, getIntent().getExtras().getLong("deviceId"));

        setTitle(device.getName());

        name.setText(device.getName());

        usagePerHour.setText(String.valueOf(device.getUsagePerHour()));
        isAutomatic.setText(device.isAutomatic() ? "Device is " : "Device is NOT ");
        macLayout.setVisibility(device.isAutomatic() ? View.VISIBLE : View.INVISIBLE);
        mac.setText(device.getMac());

        final java.sql.Date[] daysOfWeek = Util.getDaysOfWeek(new Date(), Calendar.getInstance().getFirstDayOfWeek());

        for (int i = 0; i < 7; i++) {

            String text = "";
            if (daysOfWeek[i].compareTo(Util.getDateWithOutTime(new Date())) == 0)
                text = "Today\n" + daysOfWeek[i].toString();
            else
                text = daysOfWeek[i].toString();
            weekDaysButtons.get(i).setText(text);

            final int finalI = i;
            final int finalI1 = i;
            weekDaysButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsageRecordShowDialog usageRecordShowDialog = new UsageRecordShowDialog(context, daysOfWeek[finalI], device);
                    usageRecordShowDialog.show();
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_device_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteDevice) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            CharSequence title = "Are you sure about deleting this Device?", addd = "Delete", cancell = "Cancel";
            builder.setTitle(title.toString());
            builder.setPositiveButton(addd, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final ProgressDialog pd = new ProgressDialog(context);
                            pd.setMessage("Device is removing");
                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pd.setCanceledOnTouchOutside(false);
                            pd.setCancelable(false);
                            pd.show();

                            try {
                                LocalSchedulerWebServicesCallUtil.deleteDevice(device, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        context.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, "Device Is NOT Removed", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (response.code() == 200) {
                                            device.delete();
                                            context.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "Device Is Removed-", Toast.LENGTH_LONG).show();
                                                    context.setResult(Activity.RESULT_OK);
                                                    context.finish();
                                                }
                                            });
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                pd.dismiss();
                            }

                            dialog.dismiss();
                        }
                    }
            );
            builder.setNegativeButton(cancell, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        return true;
    }
}
