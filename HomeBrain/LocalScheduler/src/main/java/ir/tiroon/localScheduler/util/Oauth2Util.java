package ir.tiroon.localScheduler.util;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class Oauth2Util {

    public String OAuthConstant_CLIENT_SECRET;
    public String OAuthConstant_CLIENT_ID;
    public String OAuthConstant_TOKEN_REQUEST_URL;
    public String Oauth2RefreshToken;

    public String gainedAccessToken = null;

    @Autowired
    MQTTUtil2 mqttUtil2;

    public Oauth2Util(String OAuthConstant_CLIENT_SECRET, String OAuthConstant_CLIENT_ID, String OAuthConstant_TOKEN_REQUEST_URL, String Oauth2RefreshToken) throws IOException {
        this.OAuthConstant_CLIENT_SECRET = OAuthConstant_CLIENT_SECRET;
        this.OAuthConstant_CLIENT_ID = OAuthConstant_CLIENT_ID;
        this.OAuthConstant_TOKEN_REQUEST_URL = OAuthConstant_TOKEN_REQUEST_URL;

        if (Oauth2RefreshToken == null)
            mqttUtil2.askForRefreshToken();

        this.Oauth2RefreshToken = Oauth2RefreshToken;


        gainAccessToken();
    }


    public void gainAccessToken() throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("refresh_token", Oauth2RefreshToken)
                .add("grant_type", "refresh_token")
                .build();

        OkHttpUtil okHttp = new OkHttpUtil(OkHttpUtil.basicAuthCredentials( OAuthConstant_CLIENT_ID, OAuthConstant_CLIENT_SECRET));

        JSONObject postResult = new JSONObject(okHttp.post(OAuthConstant_TOKEN_REQUEST_URL, formBody));

        System.out.println("BMD::>"+postResult.toString());
        gainedAccessToken = postResult.getString("access_token");

    }
}
