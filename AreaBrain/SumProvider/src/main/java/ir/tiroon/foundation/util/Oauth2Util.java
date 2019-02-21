package ir.tiroon.foundation.util;

import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RequestAuthenticator;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Oauth2Util {

    public String OAuthConstant_CLIENT_SECRET;
    public String OAuthConstant_CLIENT_ID;
    public String OAuthConstant_TOKEN_REQUEST_URL;
    public String OAuthConstant_Username;
    public String OAuthConstant_Password;

    List<String> OAuthConstant_SCOPES;

    public String gainedAccessToken = null;

    public Oauth2Util(String OAuthConstant_CLIENT_SECRET, String OAuthConstant_CLIENT_ID, String OAuthConstant_TOKEN_REQUEST_URL, String OAuthConstant_Username, String OAuthConstant_Password) throws IOException {
        this.OAuthConstant_CLIENT_SECRET = OAuthConstant_CLIENT_SECRET;
        this.OAuthConstant_CLIENT_ID = OAuthConstant_CLIENT_ID;
        this.OAuthConstant_TOKEN_REQUEST_URL = OAuthConstant_TOKEN_REQUEST_URL;
        this.OAuthConstant_Username = OAuthConstant_Username;
        this.OAuthConstant_Password = OAuthConstant_Password;

        OAuthConstant_SCOPES = new ArrayList<>();
        OAuthConstant_SCOPES.add("openid");

        gainAccessToken();
    }

    public void gainAccessToken() throws IOException {

        final String[] scopes = new String[1];
        if (OAuthConstant_SCOPES.size()==1)
            scopes[0] = OAuthConstant_SCOPES.get(0);
        else {
            OAuthConstant_SCOPES.stream().forEach(s -> scopes[0] += s + ",");
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

        String postR = okHttp.post(OAuthConstant_TOKEN_REQUEST_URL, formBody);
        System.out.println("BMD::>"+postR);

        JSONObject postResult = new JSONObject(postR);

        gainedAccessToken = postResult.getString("access_token");

    }
}
