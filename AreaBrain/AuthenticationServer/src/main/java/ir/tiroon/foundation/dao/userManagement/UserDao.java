package ir.tiroon.foundation.dao.userManagement;

import ir.tiroon.foundation.dao.AbstractDao;
import ir.tiroon.foundation.dao.AbstractDaoNeedToUseInsteadOther;
import ir.tiroon.foundation.model.userManagement.User;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by Neo BomBer on 12/11/2015.
 */


@Component
@Qualifier("userDao")
public class UserDao extends AbstractDaoNeedToUseInsteadOther<Long, User> {



    public ArrayList<User> findAllMinesOne(int userID) {

        Query q = sessionFactory.getCurrentSession().createQuery("from User where userId!=:ui");
        q.setParameter("ui", userID);

            ArrayList<User> temp = new ArrayList<User>();

//        for (User u :temp)
//            if(u.getRoles().equals(findById(userID).getRoles()))
//                temp.add(u);
//        return  temp;

        return (ArrayList<User>) q.list();
    }


    public long persistUser(User entity) {
        persist(entity);
        return entity.getId();
    }

    public long saveUser(User entity){
        save(entity);
        return entity.getId();
    }

    public User findByEmail(String email) {

        Query q = sessionFactory.getCurrentSession().createQuery("from User as u where u.email=:email");
        q.setParameter("email", email);
        ArrayList<User> users = (ArrayList<User>) q.list();

        if (users.size() > 0)
            return users.get(0);

        return null;
    }
}
