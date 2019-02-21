package ir.tiroon.localScheduler.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;


public class RefreshTokenUtil {

    static final String refreshTokenFileAddress = "C:\\WorkSpace\\SmartGridCounsumptionScheduling\\AreaBrain\\jpaillerKey\\refreshToken.bmd";


    public static void setRefreshToken(String refreshToken) throws IOException {
        File file = new File(refreshTokenFileAddress);
        FileUtils.writeStringToFile(file, refreshToken, Charset.defaultCharset());
    }

    public static String getRefreshToken() throws IOException {
        File file = new File(refreshTokenFileAddress);
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }



}
