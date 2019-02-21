package ir.tiroon.foundation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl("http://localhost:8080/authserver/oauth/check_token");
        tokenService.setClientId("sumprovider");
        tokenService.setClientSecret("sumprovidersecret");
        return tokenService;
    }


//
//    <bean id="authenticationEntryPoint" class="org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint">
//    <property name="realmName" value="myRealm" />
//</bean>

//    <oauth2:resource-server id="myResource" resource-id="myResourceId" token-services-ref="tokenServices" />

//    security:http pattern="/myPattern/**" create-session="never"
//    entry-point-ref="authenticationEntryPoint" access-decision-manager-ref="accessDecisionManager">
//    <security:anonymous enabled="false" />
//    <security:intercept-url pattern="/**" access="SCOPE_READ" method="GET" />
//    <security:intercept-url pattern="/**" access="SCOPE_READ" method="HEAD" />
//    <security:intercept-url pattern="/**" access="SCOPE_READ" method="OPTIONS" />
//    <security:intercept-url pattern="/**" access="SCOPE_WRITE" method="PUT" />
//    <security:intercept-url pattern="/**" access="SCOPE_WRITE" method="POST" />
//    <security:intercept-url pattern="/**" access="SCOPE_WRITE" method="DELETE" />
//    <security:custom-filter ref="myResource" before="PRE_AUTH_FILTER" />
//    <security:access-denied-handler ref="oauthAccessDeniedHandler" />
//    <security:expression-handler ref="oauthWebExpressionHandler" />
//</security:http>


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http
            .authorizeRequests()
                .anyRequest().authenticated();

    }

}