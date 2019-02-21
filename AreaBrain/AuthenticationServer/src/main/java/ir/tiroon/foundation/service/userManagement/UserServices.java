package ir.tiroon.foundation.service.userManagement;

import ir.tiroon.foundation.dao.userManagement.OperationDao;
import ir.tiroon.foundation.dao.userManagement.RoleDao;
import ir.tiroon.foundation.dao.userManagement.UserAccessLogDao;
import ir.tiroon.foundation.dao.userManagement.UserDao;
import ir.tiroon.foundation.model.userManagement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;


@Service("userService")
@Transactional
public class UserServices {

    @Autowired
    UserDao userDao;

    @Autowired
    UserAccessLogDao userAccessLogDao;

    @Autowired
    OperationDao operationDao;

    @Autowired
    RoleDao roleDao;

//    @Autowired
    private PasswordEncoder passwordEncoder;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public long addEntity(User user) {

            user.setState(State.Active);
            user.getRoles().add(roleDao.findById(1));
            return userDao.saveUser(user);
    }

    public User getEntityById(Long id) {
        return userDao.getByKey(id);
    }

    public ArrayList<User> getEntityList() {
        return (ArrayList<User>) userDao.findAll();
    }

    public ArrayList<User> getEntityListMinesOne(int userId) {
        return userDao.findAllMinesOne(userId);
    }


    public boolean logginCheck(String email, String pass) {

        User u = userDao.findByEmail(email);

        if (u != null && u.getPassword().equals(pass) ) {
            accessLog(u, AccessLogType.login);
            return true;
        }

        return false;
    }

    public void accessLog(User u, AccessLogType t) {

        UserAccessLog ual = new UserAccessLog();
        ual.setUser(u);
        ual.setDate(new java.sql.Date(new Date().getTime()));
        ual.setAccessLogType(t);
        userAccessLogDao.persist(ual);

    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public boolean deleteEntity(Long id) {
        userDao.delete(userDao.getByKey(id));
        return true;
    }


    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean hasPermission(Long userId, OperationType type) {
        User u = userDao.getByKey(userId);
        for (Role r : u.getRoles())
            for (Operation o : r.getOperations())
                if (o.getOperationType().equals(type))
                    return true;
        return false;
    }
}