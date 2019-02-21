package ir.tiroon.foundation.dao.userManagement;

import ir.tiroon.foundation.dao.AbstractDao;
import ir.tiroon.foundation.model.userManagement.UserAccessLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by Neo BomBer on 12/11/2015.
 */


@Component
@Qualifier("userDao")
public class UserAccessLogDao extends AbstractDao<UserAccessLog> {


    public ArrayList<UserAccessLog> findAll() {
        return (ArrayList<UserAccessLog>) sessionFactory.getCurrentSession().createQuery("from UserAccessLog").list();
    }


    public long persist(UserAccessLog entity) {
        UserAccessLog a = entity;
        sessionFactory.getCurrentSession().persist(a);
        return a.getId();
    }


    public UserAccessLog findById(long id) {
        return (UserAccessLog) sessionFactory.getCurrentSession().get(UserAccessLog.class, id);
    }


}
