package ir.tiroon.localScheduler.Scheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.tiroon.localScheduler.model.UsageRecord;
import ir.tiroon.localScheduler.service.DeviceServices;
import ir.tiroon.localScheduler.service.UsageRecordServices;
import ir.tiroon.localScheduler.util.MQTTUtil2;
import ir.tiroon.localScheduler.util.Util;
import it.sauronsoftware.cron4j.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ir.tiroon.localScheduler.util.MqttMessageRepository.mqttMessage_PublishSchedulingResult;

@Component
public class TaskSchedule {


    @Autowired
    MQTTUtil2 mqttServices;

    @Autowired
    DeviceServices deviceServices;

    @Autowired
    UsageRecordServices usageRecordServices;

    @Autowired
    OptimizationAlgorithm optimizationAlgorithm;

    Runnable schedulingOptimizationAlgorithm = () -> {
      //these operation should be processed once every day at 13:00
        try {
            optimizationAlgorithm.runOneRound();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    Runnable sendSchedulingResultsToDevices = () -> {
        //these operation should be finished until 23:59 and started at 22:00
        SchedulingUtil.getUsageRequestsWhichAreForTomorrow(usageRecordServices).stream().forEach(usageRecord -> {
                    try {
                        SchedulingUtil.tellSchedulingResultsToDevice(usageRecord, mqttServices);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    };



    Scheduler optimizationAlgorithmScheduler;

    Scheduler resultPublishingScheduler;

    public TaskSchedule(){
        optimizationAlgorithmScheduler = new Scheduler();
        resultPublishingScheduler = new Scheduler();

        optimizationAlgorithmScheduler.schedule("1 11 * * *",schedulingOptimizationAlgorithm);
        optimizationAlgorithmScheduler.start();

        //\\
        resultPublishingScheduler.schedule("5 11 * * *",sendSchedulingResultsToDevices);
        resultPublishingScheduler.start();
    }
}
