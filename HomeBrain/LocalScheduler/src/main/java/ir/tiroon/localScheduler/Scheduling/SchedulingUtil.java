package ir.tiroon.localScheduler.Scheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.tiroon.localScheduler.model.UsageRecord;
import ir.tiroon.localScheduler.service.UsageRecordServices;
import ir.tiroon.localScheduler.util.MQTTUtil2;
import ir.tiroon.localScheduler.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ir.tiroon.localScheduler.util.MqttMessageRepository.mqttMessage_PublishSchedulingResult;

public class SchedulingUtil {

    public static ArrayList<UsageRecord> getUsageRequestsWhichAreForTomorrow(UsageRecordServices usageRecordServices){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,1);
        Date tomorrow = Util.getDateWithOutTime(calendar.getTime());
        return  usageRecordServices.getUsageRecordsByDate(new java.sql.Date(tomorrow.getTime()));
    }

    public static void tellSchedulingResultsToDevice(UsageRecord usageRecord, MQTTUtil2 mqttServices) throws JsonProcessingException {
        mqttServices.sendToALocalTopic(usageRecord.getDevice().isAutomatic()? usageRecord.getDevice().getMac() : MQTTUtil2.sendToMobileTopic
                ,mqttMessage_PublishSchedulingResult(usageRecord));
    }
}
