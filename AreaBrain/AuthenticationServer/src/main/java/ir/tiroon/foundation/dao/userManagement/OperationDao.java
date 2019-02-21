package ir.tiroon.foundation.dao.userManagement;

import ir.tiroon.foundation.dao.AbstractDao;
import ir.tiroon.foundation.model.userManagement.Operation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by Neo BomBer on 12/11/2015.
 */


@Component
@Qualifier("operationDao")
public class OperationDao extends AbstractDao<Operation> {


    public ArrayList<Operation> findAll() {
        return (ArrayList<Operation>) sessionFactory.getCurrentSession().createQuery("from Operation").list();
    }

    public long persist(Operation entity) {
        Operation a = entity;
        sessionFactory.getCurrentSession().persist(a);
        return a.getId();
    }

    public Operation findById(long id) {
        return (Operation) sessionFactory.getCurrentSession().get(Operation.class, id);
    }


}
