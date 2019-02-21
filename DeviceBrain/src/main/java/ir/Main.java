package ir;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.JavaUtil.LocalSchedulerWebServicesCallUtil;
import ir.JavaUtil.MQTTUtil2;
import ir.Model.Device;
import ir.Model.UsageRecord;
import it.sauronsoftware.cron4j.Scheduler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String args[]) throws IOException {
        String deviceName = "test1";
        String deviceMac = "asds:asd";
        String deviceUsagePerHour = "200";

        Device device = new Device(deviceName, deviceMac, true, Double.valueOf(deviceUsagePerHour));

        IMqttMessageListener mqttMessageListenerForHome = (topicName, mqttMessage) -> {

            JSONObject message = new JSONObject(mqttMessage.toString());
            if (message.getString("type").equals(MQTTUtil2.homeMessageType_LocalSchedulingResult)) {

                ObjectMapper om = new ObjectMapper();
                UsageRecord ur = om.readValue(message.getString("usageRecord"), UsageRecord.class);
                device.getUsageRecords().add(ur);
            }

            System.out.println("BMD::Message:from home devices:" + topicName + ":::" + mqttMessage.toString());
        };


        MQTTUtil2 mqttUtil2 = new MQTTUtil2();


        LocalSchedulerWebServicesCallUtil.addDevice(device, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                device.setDeviceId(new ObjectMapper().readValue(response.body().string(), Device.class).getDeviceId());
                mqttUtil2.subscribeALocalTopic(device.getMac(), mqttMessageListenerForHome);
            }
        });

        java.sql.Date theDay = new java.sql.Date(new Date().getTime());

        UsageRecord usageRecord = new UsageRecord(theDay, device);

        List<Integer> zeros = new ArrayList<>(24);

        for (int i = 0; i < 24; i++) zeros.add(i, i % 2 == 0 ? 1 : 0);

        usageRecord.setScheduledVector(zeros);

        usageRecord.setRequestVector(zeros);

        usageRecord.setDate(theDay);
        //---------------------------------------------------------
        //some kind of scheduler or infinite loop is needed for using received local scheduling results

    }

    static void schedulerTest() {
        Scheduler s = new Scheduler();
        System.out.println(s.getTimeZone() + ":::" + new Date().toString() + ":::");
        // Schedule a once-a-minute task.
        s.schedule("54-58/1 16 * * *", () -> System.out.println("Another minute ticked away..."));
        // Starts the scheduler.
        s.start();

        // Will run for ten minutes.
        try {
            Thread.sleep(1000L * 60L * 10L);
        } catch (InterruptedException e) {
        }

        // Stops the scheduler.
        s.stop();

    }

}
