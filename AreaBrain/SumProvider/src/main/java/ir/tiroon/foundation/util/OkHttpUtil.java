package ir.tiroon.foundation.util;

import java.io.IOException;

import okhttp3.*;

/**
 * Created by Lenovo on 22/09/2017.
 */

public class OkHttpUtil {

    public final OkHttpClient client;

    public static String basicAuthCredentials(String email, String pass) {
        return Credentials.basic(email, pass);
    }

    public static String accessTokenCredential(String token) {
        return "bearer " + token;
    }

    public OkHttpUtil(final String credential) {
        client = new OkHttpClient.Builder()
                .authenticator((route, response) -> {
                    if (response.request().header("Authorization") != null) {
                        return null; // Give up, we've already attempted to authenticate.
                    }

                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                })
                .build();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType form_encoded
            = MediaType.parse("application/x-www-form-urlencoded;");

    public void asyncPost(String url, RequestBody rbody, Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public String post(String url, RequestBody rbody) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

    public void asyncGet(Callback callback, String url) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public void get(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }
}
