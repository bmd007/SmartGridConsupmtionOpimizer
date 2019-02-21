package ir.tiroon.localScheduler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.tiroon.localScheduler.model.UsageRecord;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;

public class MqttMessageRepository {

    public static final String homeMessageType_RefreshTokenRequired = "rtr";
    //    static final String mqttType_DeviceUsageRequest = "dur";
    public static final String homeMessageType_LocalSchedulingResult = "lsr";
//    static final String mqttType_RefreshTokenCarrier = "rtc";

    public static final String areaMessageType_NewSumIsAvailable = "nsa";
//    static final String mqttType_RefreshTokenCarrier = "rtc";



    public static String mqttMessage_RefreshTokenNeeded(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", homeMessageType_RefreshTokenRequired);
        jsonObject.put("data","Please send a refresh token for home brain");
        return jsonObject.toString();
    }


    public static String mqttMessage_PublishSchedulingResult(UsageRecord usageRecord) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", homeMessageType_LocalSchedulingResult);
        ObjectMapper objectMapper = new ObjectMapper();
        jsonObject.put("usageRecord",objectMapper.writeValueAsString(usageRecord));
        return jsonObject.toString();
    }

    //This method should be completed while the scheduling algorithm implementation is in processing
    public void newSumAvailableMessageProcessor(JSONObject jsonObject) throws IOException, ClassNotFoundException {
        Date date = new Date(jsonObject.getLong("dateTime"));
        BigInteger bigInteger = (BigInteger) Util.fromString(jsonObject.getString("sumResult"));
        /////then
    }

}
