package ir.tiroon.localScheduler.config;

import ir.tiroon.localScheduler.util.Oauth2Util;
import ir.tiroon.localScheduler.util.RefreshTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyBeans {

    @Bean
    public Oauth2Util getOauth2Util() throws IOException {
        return new Oauth2Util("homesecret", "homebrain",
                "http://localhost:8080/authserver/oauth/token", RefreshTokenUtil.getRefreshToken());
    }

}
