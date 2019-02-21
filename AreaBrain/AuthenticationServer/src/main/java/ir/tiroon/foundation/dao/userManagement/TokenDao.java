package ir.tiroon.foundation.dao.userManagement;

import ir.tiroon.foundation.dao.AbstractDaoNeedToUseInsteadOther;
import ir.tiroon.foundation.model.userManagement.LoginToken;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Repository("tokenRepository")
@Transactional
public class TokenDao extends AbstractDaoNeedToUseInsteadOther<String, LoginToken>
        implements PersistentTokenRepository {

    static final Logger logger = LoggerFactory.getLogger(TokenDao.class);

    public void createNewToken(PersistentRememberMeToken token) {
        logger.info("Creating LoginToken for user : {}", token.getUsername());
        LoginToken persistentLogin = new LoginToken();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        persist(persistentLogin);

    }

    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        logger.info("Fetch LoginToken if any for seriesId : {}", seriesId);
        try {
            Criteria crit = createEntityCriteria();
            crit.add(Restrictions.eq("series", seriesId));
            LoginToken persistentLogin = (LoginToken) crit.uniqueResult();

            return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                    persistentLogin.getToken(), persistentLogin.getLast_used());
        } catch (Exception e) {
            logger.info("LoginToken not found...");
            return null;
        }
    }

    public void removeUserTokens(String username) {
        logger.info("Removing LoginToken if any for user : {}", username);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("username", username));
        LoginToken persistentLogin = (LoginToken) crit.uniqueResult();
        if (persistentLogin != null) {
            logger.info("rememberMe was selected");
            delete(persistentLogin);
        }

    }

    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        logger.info("Updating LoginToken for seriesId : {}", seriesId);
        LoginToken persistentLogin = getByKey(seriesId);
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        update(persistentLogin);
    }

}