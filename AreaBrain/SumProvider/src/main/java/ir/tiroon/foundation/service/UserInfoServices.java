package ir.tiroon.foundation.service;
import ir.tiroon.foundation.dao.UserInfoDao;
import ir.tiroon.foundation.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service("userInfoService")
@Transactional
public class UserInfoServices {

    @Autowired
    UserInfoDao userDao;

    public void addEntity(UserInfo entity) {
        userDao.persist(entity);
    }

    public UserInfo getEntityById(String email) {
        return userDao.getByKey(email);
    }

    public ArrayList<UserInfo> getEntityList() {
        return (ArrayList<UserInfo>) userDao.findAll();
    }


    public void updateUser(UserInfo user) {
        userDao.update(user);
    }

    public boolean deleteEntity(UserInfo user) {
        userDao.delete(user);
        return true;
    }

}