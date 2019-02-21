package ir.tiroon.schedulingApp.JavaUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Oauth2Util {

    public String OAuthConstant_CLIENT_SECRET;
    public String OAuthConstant_CLIENT_ID;
    public String OAuthConstant_TOKEN_REQUEST_URL;
    public String OAuthConstant_Username;
    public String OAuthConstant_Password;

    List<String> OAuthConstant_SCOPES;

    public Oauth2Util(String OAuthConstant_CLIENT_SECRET, String OAuthConstant_CLIENT_ID, String OAuthConstant_TOKEN_REQUEST_URL, String OAuthConstant_Username, String OAuthConstant_Password) throws IOException, JSONException {
        this.OAuthConstant_CLIENT_SECRET = OAuthConstant_CLIENT_SECRET;
        this.OAuthConstant_CLIENT_ID = OAuthConstant_CLIENT_ID;
        this.OAuthConstant_TOKEN_REQUEST_URL = OAuthConstant_TOKEN_REQUEST_URL;
        this.OAuthConstant_Username = OAuthConstant_Username;
        this.OAuthConstant_Password = OAuthConstant_Password;

        OAuthConstant_SCOPES = new ArrayList<>();
        OAuthConstant_SCOPES.add("openid");
    }

    public String gainRefreshToken() throws IOException, JSONException {

        final String[] scopes = new String[1];
        if (OAuthConstant_SCOPES.size()==1)
            scopes[0] = OAuthConstant_SCOPES.get(0);
        else {
            for(String s :OAuthConstant_SCOPES)
                    scopes[0] += s + ",";
            scopes[0].substring(scopes[0].length()-2,scopes[0].length()-1);
        }
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", OAuthConstant_CLIENT_ID)
                .add("client_secret", OAuthConstant_CLIENT_SECRET)
                .add("scope", scopes[0])
                .add("grant_type", "password")
                .add("username", OAuthConstant_Username)
                .add("password", OAuthConstant_Password)
                .build();

        OkHttpUtil okHttp = new OkHttpUtil(OkHttpUtil.basicAuthCredentials( OAuthConstant_CLIENT_ID, OAuthConstant_CLIENT_SECRET));

        JSONObject postResult = new JSONObject(okHttp.post(OAuthConstant_TOKEN_REQUEST_URL, formBody, okHttp.client));

        return postResult.getString("refresh_token");
    }
}
