package ir.tiroon.foundation.restController;

import ir.tiroon.foundation.model.UserInfo;
import ir.tiroon.foundation.paillier.jpaillier.PublicKey;
import ir.tiroon.foundation.service.SumProvisionService;
import ir.tiroon.foundation.service.UserInfoServices;
import ir.tiroon.foundation.util.JpaillierKeyProvider;
import ir.tiroon.foundation.util.MQTTUtil2;
import ir.tiroon.foundation.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Future;

@RestController
public class RestControllers {


    @Autowired
    UserInfoServices userInfoServices;

    @Autowired
    MQTTUtil2 mqttService;

    @Autowired
    JpaillierKeyProvider jpaillierKeyProvider;

    @Autowired
    SumProvisionService paillerService;


//    @RequestMapping(value = "/addpostcode/{postcode}", method = RequestMethod.GET,consumes = "application/x-www-form-urlencoded")
//    @ResponseBody
//    public ResponseEntity<String> createEmployee(@RequestBody String postAddress, Authentication authentication) throws Exception {





    @RequestMapping(value = "/addPostCode/{pc}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> addPostCode(@PathVariable("pc") String postAddress, Authentication authentication) throws Exception {

        UserInfo userInfo = userInfoServices.getEntityById(authentication.getPrincipal().toString());

        if (userInfo == null) {
            userInfo = new UserInfo(authentication.getPrincipal().toString(), postAddress);
            userInfoServices.addEntity(userInfo);
        } else {
            userInfo.setPostAddress(postAddress);
            userInfoServices.updateUser(userInfo);
        }

        mqttService.subscribeATopic(userInfo.getPostAddress() + "_s");
        mqttService.sendToTopic(userInfo.getPostAddress() + "_r", "BMDD bmddd");

        ResponseEntity entity = new ResponseEntity(userInfo, HttpStatus.OK);

        return entity;
    }



    @RequestMapping(value = "/getPaillierPK", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> getPaillierPublicKey() throws Exception {
        PublicKey pk = jpaillierKeyProvider.keyPair.getPublicKey();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("n",Util.objectToString(pk.getN()));
        jsonObject.put("nSquared",Util.objectToString(pk.getnSquared()));
        jsonObject.put("g",Util.objectToString(pk.getG()));
        jsonObject.put("bits",pk.getBits());

        return new ResponseEntity(jsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/tellLocalSchedulingResult", method = RequestMethod.POST, consumes = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> tellLocalSchedulingResult(@RequestBody String input, Authentication authentication) throws Exception {

        JSONObject jsonObject = new JSONObject(input);

        String receivedAdvertise = (String)jsonObject.get("localSum");
        JSONArray ja = jsonObject.getJSONArray("localSumPerHourJsonArray");

        ArrayList<BigInteger> encryptedLocalRequestSumsHourly = new ArrayList<>(24);
        for(int i=0; i<24;i++)
            encryptedLocalRequestSumsHourly.add(i, (BigInteger) Util.fromString(ja.getString(i)));

        UserInfo userInfo = userInfoServices.getEntityById(authentication.getPrincipal().toString());

        userInfo.setLastEncryptedLocalSchedulingResult(receivedAdvertise);
        userInfo.setLastEncryptedLocalSumOfRequests(encryptedLocalRequestSumsHourly);

        userInfoServices.updateUser(userInfo);


        //below this line should work asynchronous--maybe simply another thread
        BigInteger decryptedSumResult = paillerService.decrypt(paillerService.sumOverAllUserSchedulingRequestsSums());


        JSONObject jo = new JSONObject();
        jo.put("sumResult", decryptedSumResult.intValueExact());
            JSONArray jsr = new JSONArray();
        ArrayList<BigInteger> encryptedSumVectorOnAllUsers = paillerService.sumOverAllUsersSchedulingResultVectors();
        for(int i =0 ; i<24; i++)
                jsr.put(i,paillerService.decrypt(encryptedSumVectorOnAllUsers.get(i)).intValueExact());

        jo.put("areaSumPerHourJsonArray",jsr);

        mqttService.sendToTopic("areaBrain_sumProvider_Topic",jo.toString());
        //-------------------------
        //the big Integer Should Be Removed From here
        return new ResponseEntity(input, HttpStatus.OK);
    }

}
