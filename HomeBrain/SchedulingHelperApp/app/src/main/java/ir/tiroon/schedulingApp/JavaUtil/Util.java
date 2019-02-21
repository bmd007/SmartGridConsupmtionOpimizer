package ir.tiroon.schedulingApp.JavaUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import ir.tiroon.schedulingApp.Services.MQTTService;
import ir.tiroon.schedulingApp.Util.MyUtil;

public class Util {


    public static String base_url = "192.168.1.3";

    public static void setBaseUrls(String base_url, Context context){
        Util.base_url = base_url;
        LocalSchedulerWebServicesCallUtil.localSchedulerBaseURL = "http://"+Util.base_url+":8082/scheduler/";
        LocalSchedulerWebServicesCallUtil.authServerBaseURL = "http://"+Util.base_url+":8080/authserver/";
        if (!MyUtil.isServiceRunning(context))
            context.stopService(new Intent(context, MQTTService.class));

        context.startService(new Intent(context, MQTTService.class));

    }
    public static java.sql.Date[] getDaysOfWeek(Date refDate, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        java.sql.Date[] daysOfWeek = new java.sql.Date[7];
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = new java.sql.Date(getDateWithOutTime(calendar.getTime()).getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    public static Date getDateWithOutTime(Date targetDate) {
        Calendar newDate = Calendar.getInstance();
        newDate.setLenient(false);
        newDate.setTime(targetDate);
        newDate.set(Calendar.HOUR_OF_DAY, 0);
        newDate.set(Calendar.MINUTE,0);
        newDate.set(Calendar.SECOND,0);
        newDate.set(Calendar.MILLISECOND,0);

        return newDate.getTime();

    }

    public static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(s,Base64.DEFAULT);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Write the object to a Base64 string.
     */
    public static String objectToString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
    }

}
