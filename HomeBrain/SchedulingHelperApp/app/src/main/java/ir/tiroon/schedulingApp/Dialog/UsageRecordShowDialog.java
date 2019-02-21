package ir.tiroon.schedulingApp.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.tiroon.schedulingApp.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.tiroon.schedulingApp.JavaUtil.Util;
import ir.tiroon.schedulingApp.Model.Device;
import ir.tiroon.schedulingApp.Model.UsageRecord;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import schedulingApp.tiroon.ir.R;

public class UsageRecordShowDialog extends Dialog {

    @BindView(R.id.submitUsageRecord) Button saveButton;
    @BindView(R.id.contentBoxInUsageRecordDialog) GridLayout requestVectorGridLayout;
    @BindView(R.id.scheduledBoxInUsageRecordDialog) GridLayout scheduledVectorGridLayout;

    Activity context;
    UsageRecord usageRecord;

    List<CheckBox> requestVectorBoxes = new ArrayList<>(24);
    List<CheckBox> scheduledVectorBoxes = new ArrayList<>(24);

    public UsageRecordShowDialog(final Activity context, final java.sql.Date theDay, Device device) {
        super(context);

        this.context = context;

        setContentView(R.layout.usage_record_show_dialog);

        ButterKnife.bind(this);

        /////////
        final List<UsageRecord> usageRecords = Select.from(UsageRecord.class).list();

        usageRecord = null;
        for (UsageRecord ur : usageRecords) {
            boolean b1 = ur.getDevice().getId().longValue() == device.getId().longValue();
            boolean b2 = Util.getDateWithOutTime(ur.getDate()).getTime() == Util.getDateWithOutTime(theDay).getTime();
            if (b1 && b2) {
                usageRecord = ur;
                break;
            }
        }


        if (usageRecord == null) {
            usageRecord = new UsageRecord(theDay, device);
            List<Integer> zeros = new ArrayList<>(24);
            for (int i = 0; i < 24; i++) zeros.add(i,1);
            usageRecord.setScheduledVector(zeros);
            usageRecord.setRequestVector(zeros);
            usageRecord.setDate(theDay);
        }


        /////////////////
        setTitle("Usage Vectors for "+usageRecord.getDate());

        final Date today = new Date();
        for(int i=0;i<24;i++){
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(String.valueOf(i+1));
            checkBox.setChecked(usageRecord.getRequestVector().get(i) == 1);
            checkBox.setClickable(today.compareTo(theDay) < 0);
            requestVectorBoxes.add(checkBox);
        }

        for(int i=0;i<24;i++){
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(String.valueOf(i+1));
            checkBox.setClickable(false);
            checkBox.setChecked(usageRecord.getScheduledVector().get(i) == 1);
            scheduledVectorBoxes.add(checkBox);
        }

        for (CheckBox cb: requestVectorBoxes)
            requestVectorGridLayout.addView(cb);


        for (CheckBox cb: scheduledVectorBoxes)
            scheduledVectorGridLayout.addView(cb);




        saveButton.setVisibility(
                today.compareTo(theDay) < 0 ? View.VISIBLE : View.INVISIBLE);

        //--------------------------------------------------------
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("Usage Record is adding");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();

                final List<Integer> requestValues = new ArrayList<>(24);
                for(int i=0; i<24;i++)
                    requestValues.add(i, requestVectorBoxes.get(i).isChecked() ? 1 : 0);

                usageRecord.setRequestVector(requestValues);
                try {
                    LocalSchedulerWebServicesCallUtil.tellUsageRequestVector(usageRecord, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                           context.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(context, "Usage Record has not added", Toast.LENGTH_SHORT).show();
                                 }
                           });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            usageRecord.setId(new ObjectMapper().readValue(response.body().string(), UsageRecord.class).getId());
                            usageRecord.save();
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Usage Record has sent and added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    pd.dismiss();
                }
            }
        });
    }

    boolean checkDateForShowSaveButton(Date today,Date theDay) {

        boolean b1 = today.compareTo(theDay) >= 0; //today is >= theDay

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        boolean b2 = calendar.get(Calendar.HOUR_OF_DAY) > 13;//current Hour is after 13
        calendar.add(Calendar.DATE,1); //set Calendar Date to tomorrow
        boolean b3 = theDay.compareTo(calendar.getTime()) == 0; //the day is tomorrow

        return !( b1 || (b2 && b3) );
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
