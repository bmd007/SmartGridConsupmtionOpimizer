package ir.tiroon.foundation.util;

import ir.tiroon.foundation.paillier.jpaillier.KeyPair;
import ir.tiroon.foundation.paillier.jpaillier.KeyPairBuilder;
import ir.tiroon.foundation.paillier.jpaillier.PublicKey;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;

@Component
public class JpaillierKeyProvider {

    public final String keyFileAddress = "C:\\WorkSpace\\SmartGridCounsumptionScheduling\\AreaBrain\\jpaillerKey\\keyPair.bmd";

    public KeyPair keyPair;

    public JpaillierKeyProvider() throws IOException, ClassNotFoundException {

        KeyPairBuilder keyPairBuilder = new KeyPairBuilder();

        File file = new File(keyFileAddress);

        if (file.exists()) {
            String s = FileUtils.readFileToString(file, Charset.defaultCharset());
            keyPair = (KeyPair)Util.fromString(s);
        } else {
            keyPair = keyPairBuilder.generateKeyPair();
            FileUtils.writeStringToFile(file,Util.objectToString(keyPair), Charset.defaultCharset());
        }
    }


}
