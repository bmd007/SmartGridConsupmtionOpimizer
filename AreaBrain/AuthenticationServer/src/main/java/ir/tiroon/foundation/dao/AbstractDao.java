package ir.tiroon.foundation.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo BomBer on 12/10/2015.
 */
//@Transactional
@Component
public abstract class AbstractDao<T> implements Serializable {

    @Autowired
    protected SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    abstract public long persist(T entity);

    public void saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().persist(entity);
    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) delete(entity);
    }

    public abstract ArrayList<T> findAll();

    public abstract T findById(long id);

}
