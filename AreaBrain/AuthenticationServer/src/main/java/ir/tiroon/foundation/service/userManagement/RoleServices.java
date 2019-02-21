package ir.tiroon.foundation.service.userManagement;

import ir.tiroon.foundation.dao.userManagement.RoleDao;
import ir.tiroon.foundation.model.userManagement.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service("roleService")
@Transactional
public class RoleServices {

    @Autowired
    RoleDao roleDao;


    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public long addEntity(Role employee) {
        return roleDao.persist(employee);
    }

    public Role getEntityById(int id) {
        return roleDao.findById(id);
    }

    public ArrayList<Role> getEntityList() {
        return roleDao.findAll();
    }

    public void updateRole(Role role) {
        roleDao.saveOrUpdate(role);
    }

    public boolean deleteEntity(int id) {
        roleDao.delete(roleDao.findById(id));
        return true;
    }


}