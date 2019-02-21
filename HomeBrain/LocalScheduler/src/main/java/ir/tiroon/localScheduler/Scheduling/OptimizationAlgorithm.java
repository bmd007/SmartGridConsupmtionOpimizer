package ir.tiroon.localScheduler.Scheduling;

import ir.tiroon.localScheduler.model.UsageRecord;
import ir.tiroon.localScheduler.service.UsageRecordServices;
import ir.tiroon.localScheduler.util.*;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static ir.tiroon.localScheduler.util.OkHttpUtil.JSON;

@Component
public class OptimizationAlgorithm {

    Integer lastFullSumOnArea;

    @Autowired
    UsageRecordServices usageRecordServices;

    Integer lastAreaSumPerHour[] = new Integer[24];

    @Autowired
    Oauth2Util oauth2Util;


    public OptimizationAlgorithm() {
        lastFullSumOnArea = 1000;
    }


    public void newArealSumReceived(String message) throws Exception {

        JSONObject jo = new JSONObject(message);
        lastFullSumOnArea = jo.getInt("sumResult");

        JSONArray areaSumPerHourJsonArray = jo.getJSONArray("areaSumPerHourJsonArray");

        for (int i = 0; i < 24; i++) {
            lastAreaSumPerHour[i] = areaSumPerHourJsonArray.getInt(i);
            System.out.println("BMD::r from sumP:" + areaSumPerHourJsonArray.getInt(i));
        }

        if (checkWhetherAnotherRoundIsNeeded())
            runOneRound();
    }


    public void runOneRound() throws Exception {

        optimizeRequestVectors();
        //////////////////////////////////
        oauth2Util.gainAccessToken();
        PaillierUtil.gainPublicKey(oauth2Util.gainedAccessToken);
        ////////////////preparing the results of this round then send them to sum provider
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();

        ArrayList<Double> localSumPerHour = new ArrayList<>(24);
        for (int i = 0; i < 24; i++)
            localSumPerHour.add((double) 0);

        ArrayList<UsageRecord> tomorrowUsageRecords =
                usageRecordServices.getUsageRecordsByDate(new Date(Util.getTomorrowDate().getTime()));


        final Double[] temp = new Double[1];
        for (int j = 0; j < 24; j++) {
            temp[0] = 0.0;

            int finalJ = j;
            tomorrowUsageRecords.stream().forEach(usageRecord ->
                    temp[0] += (usageRecord.getRequestVector().get(finalJ) * usageRecord.getDevice().getUsagePerHour()));

            System.out.println("BMD::>> local sum on hour:" + finalJ + " is:" + temp[0]);
            localSumPerHour.set(j, temp[0]);
            ja.put(j, Util.objectToString(PaillierUtil.encrypt(BigInteger.valueOf(temp[0].longValue()))));
        }

        jo.put("localSumPerHourJsonArray", ja);
        jo.put("localSum", Util.objectToString(PaillierUtil.encrypt(BigInteger.valueOf(150))));


        RequestBody body = RequestBody.create(JSON, jo.toString());
        OkHttpUtil okHttp = new OkHttpUtil(OkHttpUtil.accessTokenCredential(oauth2Util.gainedAccessToken));
        System.out.println("BMD:abc:" + okHttp.post("http://localhost:8081/sump/tellLocalSchedulingResult", body));


    }


    //on each execution of this method, scheduledVectors of usageRecords should be set
    void optimizeRequestVectors() {
        ArrayList<UsageRecord> usageRecordsByDate = usageRecordServices.getUsageRecordsByDate(new Date(Util.getTomorrowDate().getTime()));
        for (UsageRecord usageRecord : usageRecordsByDate) {

            boolean isScheduleVectorUnTouched = true;
            for (Integer temp : usageRecord.getScheduledVector())
                if (temp != 0) {
                    isScheduleVectorUnTouched = false;
                    break;
                }
                List<Integer> scheduledVector;

            if (isScheduleVectorUnTouched == true)
                scheduledVector = usageRecord.getRequestVector();
            else
                scheduledVector = usageRecord.getScheduledVector();


            for (int i = 0; i < 24; i++)
                scheduledVector.set(i, scheduledVector.get(i) == 1 ? 0 : 1);

            usageRecord.setScheduledVector(scheduledVector);

            usageRecordServices.saveOrUpdate(usageRecord);
        }
    }


    private boolean checkWhetherAnotherRoundIsNeeded() {
        return Util.hourOfToday() < 22;
    }
}
