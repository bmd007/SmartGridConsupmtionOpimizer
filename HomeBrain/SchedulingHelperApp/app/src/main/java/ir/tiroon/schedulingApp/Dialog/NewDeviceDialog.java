package ir.tiroon.schedulingApp.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.tiroon.schedulingApp.Fragment.DeviceListFragment;
import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.Model.Device;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import schedulingApp.tiroon.ir.R;

public class NewDeviceDialog extends Dialog {

    @BindView(R.id.deviceNameEditText)
    EditText dName;
    @BindView(R.id.usagePerHourEditText)
    EditText uphour;
    @BindView(R.id.addDeviceButton)
    Button saveButton;


    Activity context;

    public NewDeviceDialog(final Activity context) {
        super(context);

        this.context = context;

        setTitle("Enter Device Information");

        setContentView(R.layout.new_device_dialog);


        ButterKnife.bind(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Device device = new Device();
                device.setAutomatic(false);
                device.setMac(String.valueOf(new SecureRandom().nextInt()));
                device.setName(dName.getText().toString());
                device.setUsagePerHour(Double.parseDouble(uphour.getText().toString().trim()));

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Device is adding");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();

                try {
                    LocalSchedulerWebServicesCallUtil.addDevice(device, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Device has not added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            device.setId(new ObjectMapper().readValue(response.body().string(), Device.class).getId());
                            device.save();

                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Device " + device.getId() + " has added", Toast.LENGTH_SHORT).show();
                                    DeviceListFragment.needRefresh = false;
                                    context.recreate();
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    pd.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
