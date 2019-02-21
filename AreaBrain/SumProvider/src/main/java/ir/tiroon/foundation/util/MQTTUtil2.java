package ir.tiroon.foundation.util;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Future;

@Component
public class MQTTUtil2 {

    Oauth2Util oauth2Util;

    String topic = "areaBrain_sumProvider_Topic";
    int qos = 2;
    MemoryPersistence persistence = new MemoryPersistence();

    MqttClient mqttClient;

    public MQTTUtil2() throws Exception {

        oauth2Util = new Oauth2Util("sumprovidersecret", "sumprovider",
                "http://localhost:8080/authserver/oauth/token",
                "b@b.com", "b");


        mqttClient = new MqttClient("tcp://localhost:1883",new Random().nextLong()+":", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(oauth2Util.OAuthConstant_CLIENT_ID);
        connOpts.setPassword(oauth2Util.gainedAccessToken.toCharArray());
        mqttClient.connect(connOpts);

        sendToTopic(topic, "get up homes get up");

    }

    public void subscribeATopic(String topicName) throws Exception {
        mqttClient.subscribe(topicName, qos, (s, mqttMessage) -> {
            System.out.println("BMD::Message:" + s + ":::" + mqttMessage.toString());
        });
    }

    public void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public void sendToTopic(String topicName, String data) throws Exception {
        MqttMessage message = new MqttMessage(data.getBytes());
        message.setQos(qos);
        mqttClient.publish(topicName, message);
    }

    IMqttMessageListener mqttMessageListener = (topicName, mqttMessage) -> {

    };

}
