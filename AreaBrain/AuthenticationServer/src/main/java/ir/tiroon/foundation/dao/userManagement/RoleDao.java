package ir.tiroon.foundation.dao.userManagement;

import ir.tiroon.foundation.dao.AbstractDao;
import ir.tiroon.foundation.model.userManagement.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by Neo BomBer on 12/11/2015.
 */


@Component
@Qualifier("roleDao")
public class RoleDao extends AbstractDao<Role> {

    public ArrayList<Role> findAll() {
        return (ArrayList<Role>) sessionFactory.getCurrentSession().createQuery("from Role").list();
    }


    public long persist(Role entity) {
        Role a = entity;
        sessionFactory.getCurrentSession().persist(a);
        return a.getId();
    }


    public Role findById(long id) {
        return (Role) sessionFactory.getCurrentSession().get(Role.class, id);
    }

}
