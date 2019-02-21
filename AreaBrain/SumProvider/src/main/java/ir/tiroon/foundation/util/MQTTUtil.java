package ir.tiroon.foundation.util;

import okhttp3.Call;
import okhttp3.Response;
import org.eclipse.jetty.util.FuturePromise;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.fusesource.mqtt.client.*;

import java.util.Random;

public class MQTTUtil {


    private final BlockingConnection connection;
    Oauth2Util oauth2Util;

    public MQTTUtil() throws Exception {

        oauth2Util = new Oauth2Util("sumprovidersecret", "sumprovider",
                "http://192.168.1.3:8080/authserver/oauth/token",
                "b@b.com", "b");

        MQTT mqtt = new MQTT();

        mqtt.setHost("localhost", 1883);
        mqtt.setClientId(new Random().nextLong()+"::");
        mqtt.setUserName("sumprovider");
        mqtt.setPassword(oauth2Util.gainedAccessToken);


        connection = mqtt.blockingConnection();

        connection.connect();

        subscribeATopic("areaBrain_sumProvider_Topic");

        sendToTopic("areaBrain_sumProvider_Topic","bmbhlhkbhlblkblk");

    }

    public void subscribeATopic(String topicName) throws Exception {
        connection.subscribe(new Topic[]{new Topic(topicName, QoS.EXACTLY_ONCE)});
    }


    public void unSubscribeATopic(String topicName) throws Exception {
        connection.unsubscribe(new String[]{topicName});
    }

    public void sendToTopic(String topicName, String data) throws Exception {
        connection.publish(topicName, data.getBytes(), QoS.EXACTLY_ONCE, false);
    }


//    public void asyncSubscribeATopic(String topicName) throws Exception {
//
//        Topic topic = new Topic(topicName, QoS.EXACTLY_ONCE);
//
//        Future<byte[]> f2 = connection.subscribe(new Topic[]{topic});
//        byte[] qoses = f2.await();
//
//    }
//                System.out.println("BMD::Message::connection created");
//
//        subscribeATopic("aa:aa:aa:aa:aa:aa:aa:aa");
//
//
//        connection.receive().then(new Callback<Message>() {
//            @Override
//            public void onSuccess(Message value) {
//
//                System.out.println("BMD::Message::"+value);
//
//                value.ack();
//            }
//
//            @Override
//            public void onFailure(Throwable value) {
//
//            }
//        });
//
//        sendToTopic("aa:aa:aa:aa:aa:aa:aa:aa","bmbhlhkbhlblkblk");
//
//        System.out.println("BMD::Message::send after subscribe");

}
