package ir.tiroon.localScheduler.util;

import ir.tiroon.localScheduler.paillier.PublicKey;
import org.json.JSONObject;

import java.math.BigInteger;


public class PaillierUtil {

    static PublicKey gainedPublicKey = null;

    public static void gainPublicKey(String oauth2AccessToken) throws Exception {

        OkHttpUtil okHttp = new OkHttpUtil(OkHttpUtil.accessTokenCredential(oauth2AccessToken));

        String pks = okHttp.get("http://localhost:8081/sump/getPaillierPK");

        System.out.println("BMD:::"+pks);

        JSONObject jo2 = new JSONObject(pks);

        gainedPublicKey = new PublicKey(
                (BigInteger) Util.fromString(jo2.get("n").toString()),
                (BigInteger) Util.fromString(jo2.get("nSquared").toString()),
                (BigInteger) Util.fromString(jo2.get("g").toString()),
                jo2.getInt("bits")
        );
    }

    public static BigInteger encrypt(BigInteger data) throws Exception {

        return gainedPublicKey.encrypt(data);
    }

}
