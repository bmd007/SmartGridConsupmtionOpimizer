package ir.JavaUtil;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

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
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        if (response.request().header("Authorization") != null) {
                            return null; // Give up, we've already attempted to authenticate.
                        }

                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType form_encoded
            = MediaType.parse("application/x-www-form-urlencoded;");

    public static void asyncPost(String url, RequestBody rbody, OkHttpClient client,Callback callback) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String post(String url, RequestBody rbody, OkHttpClient client) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();

    }

    public static void asyncGet(Callback callback, String url,OkHttpClient client) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public static String get(String url, OkHttpClient client) throws Exception {
        Request request = new Request.Builder().url(url).addHeader("accept","application/json; charset=utf-8").build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }
}
