package ir.JavaUtil;


import com.fasterxml.jackson.databind.ObjectMapper;

import ir.Model.Device;
import ir.Model.UsageRecord;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Lenovo on 21/10/2017.
 */

public class LocalSchedulerWebServicesCallUtil {

    public static OkHttpClient homeClient = new OkHttpClient.Builder().build();

    public static String localSchedulerBaseURL = "http://192.168.1.3:8082/scheduler/";

    public static void tellUsageRequestVector(UsageRecord usageRecord, Callback callback) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(OkHttpUtil.JSON, mapper.writeValueAsBytes(usageRecord));
        OkHttpUtil.asyncPost(localSchedulerBaseURL + "tellRequestVector", requestBody, homeClient, callback);
    }

    public static void deviceById(Callback callback, Long id) throws IOException {
        OkHttpUtil.asyncGet(callback, localSchedulerBaseURL + "device/" + id, homeClient);
    }

    public static void deleteDevice(Device device, Callback callback) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(OkHttpUtil.JSON, mapper.writeValueAsBytes(device));
        OkHttpUtil.asyncPost(localSchedulerBaseURL + "deleteDevice", requestBody, homeClient, callback);
    }


    public static void addDevice(Device device, Callback callback) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(OkHttpUtil.JSON, mapper.writeValueAsBytes(device));
        OkHttpUtil.asyncPost(localSchedulerBaseURL + "addDevice", requestBody, homeClient, callback);
    }

}
