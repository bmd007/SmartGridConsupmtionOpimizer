package ir.JavaUtil;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;


public class MQTTUtil2 {

    public static final String homeMessageType_LocalSchedulingResult = "lsr";

    public static String sendToMobileTopic = "mobile_app_topic";

    public static String homeBrokerURL = "tcp://192.168.1.3:1883";

    int qos = 2;
    MemoryPersistence persistence = new MemoryPersistence();

    private MqttClient mqttHomeClient;

    public MQTTUtil2() {
        try {
            mqttHomeClient = new MqttClient(homeBrokerURL, new Random().toString(), persistence);

            mqttHomeClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribeALocalTopic(String topicName, IMqttMessageListener listener){
        try {
            mqttHomeClient.subscribe(topicName, qos, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribeALocalTopic(String topicName){
        try {
            mqttHomeClient.unsubscribe(topicName);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
