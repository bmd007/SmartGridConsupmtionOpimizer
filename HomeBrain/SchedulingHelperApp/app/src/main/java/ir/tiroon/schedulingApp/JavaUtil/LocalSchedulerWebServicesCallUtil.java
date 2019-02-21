package ir.tiroon.schedulingApp.JavaUtil;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;

import ir.tiroon.schedulingApp.Model.Device;
import ir.tiroon.schedulingApp.Model.UsageRecord;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Lenovo on 21/10/2017.
 */

public class LocalSchedulerWebServicesCallUtil {

    public static String loginStatus = "offline";
    public static OkHttpClient homeClient = new OkHttpClient.Builder().build();

    public static String localSchedulerBaseURL = "http://"+Util.base_url+":8082/scheduler/";
    public static String authServerBaseURL = "http://"+Util.base_url+":8080/authserver/";


    public static void tellUsageRequestVector(UsageRecord usageRecord, Callback callback) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(OkHttpUtil.JSON, mapper.writeValueAsBytes(usageRecord));
        OkHttpUtil.asyncPost(localSchedulerBaseURL + "tellRequestVector", requestBody, homeClient, callback);
    }

    public static void usageRecordList(Callback callback) throws IOException {
        OkHttpUtil.asyncGet(callback, localSchedulerBaseURL + "usageRequestList", homeClient);
    }

    public static void deviceById(Callback callback, Long id) throws IOException {
        OkHttpUtil.asyncGet(callback, localSchedulerBaseURL + "device/" + id, homeClient);
    }

    public static String deviceList() throws Exception {
        return OkHttpUtil.get(localSchedulerBaseURL + "usageRequestList", homeClient);
    }

    public static void deleteDevice(Device device, Callback callback) throws IOException {
        OkHttpUtil.asyncGet(callback,localSchedulerBaseURL + "deleteDevice/"+device.getId(),homeClient);
    }


    public static void addDevice(Device device, Callback callback) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(OkHttpUtil.JSON, mapper.writeValueAsBytes(device));
        OkHttpUtil.asyncPost(localSchedulerBaseURL + "addDevice", requestBody, homeClient, callback);
    }


}
