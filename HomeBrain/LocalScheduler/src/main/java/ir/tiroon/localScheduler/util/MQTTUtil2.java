package ir.tiroon.localScheduler.util;

import ir.tiroon.localScheduler.Scheduling.OptimizationAlgorithm;
import ir.tiroon.localScheduler.Scheduling.SchedulingUtil;
import ir.tiroon.localScheduler.model.Device;
import ir.tiroon.localScheduler.model.UsageRecord;
import ir.tiroon.localScheduler.service.DeviceServices;
import ir.tiroon.localScheduler.service.UsageRecordServices;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static ir.tiroon.localScheduler.util.MqttMessageRepository.mqttMessage_PublishSchedulingResult;
import static ir.tiroon.localScheduler.util.MqttMessageRepository.mqttMessage_RefreshTokenNeeded;

@Component
public class MQTTUtil2 {

    Oauth2Util oauth2Util;

    String topic = "areaBrain_sumProvider_Topic";

    public static String sendToMobileTopic = "mobile_app_topic";

    int qos = 2;
    MemoryPersistence persistence = new MemoryPersistence();

    private MqttClient mqttAreaClient,mqttHomeClient;

    public MQTTUtil2() throws Exception {

        mqttHomeClient = new MqttClient("tcp://192.168.1.2:1883", new Random().nextInt()+":", persistence);
        mqttHomeClient.connect();

        //----------------------------------------------below: area broker connection:
        oauth2Util = new Oauth2Util("homesecret", "homebrain",
                "http://localhost:8080/authserver/oauth/token",RefreshTokenUtil.getRefreshToken());

        System.out.println("BMM:accessToken:"+oauth2Util.gainedAccessToken);
        //area broker url would be different in future
        mqttAreaClient = new MqttClient("tcp://localhost:1883",new Random().nextLong()+"::", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(oauth2Util.OAuthConstant_CLIENT_ID);
        connOpts.setPassword(oauth2Util.gainedAccessToken.toCharArray());
        mqttAreaClient.connect(connOpts);

        mqttAreaClient.subscribe(topic, qos, mqttMessageListenerForArea);

        //-----------------------------------------------------
    }

    public void subscribeALocalTopic(String topicName) throws Exception {
        mqttHomeClient.subscribe(topicName, qos, mqttMessageListenerForHome);
    }

    public void unSubscribeALocalTopic(String topicName) throws Exception {
        mqttHomeClient.unsubscribe(topicName);
    }

    public void sendToALocalTopic(String topicName, String data){
        MqttMessage message = new MqttMessage(data.getBytes());
        message.setQos(qos);
        try {
            mqttHomeClient.publish(topicName, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void askForRefreshToken(){
        sendToALocalTopic(sendToMobileTopic,mqttMessage_RefreshTokenNeeded());
    }


    @Autowired
    OptimizationAlgorithm optimizationAlgorithm;

    IMqttMessageListener mqttMessageListenerForArea = (topicName, mqttMessage) -> {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        if( calendar.get(Calendar.HOUR_OF_DAY) < 22 )
            optimizationAlgorithm.newArealSumReceived(mqttMessage.toString());

    };


    IMqttMessageListener mqttMessageListenerForHome = (topicName, mqttMessage) ->
            System.out.println("BMD::Message:from home devices:" + topicName + ":::" + mqttMessage.toString());

}
