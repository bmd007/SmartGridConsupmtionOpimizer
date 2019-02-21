package ir.tiroon.localScheduler.dao;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
public abstract class AbstractDao<PK extends Serializable, T> {


    //        Query q = getSession().createQuery
//                ("select rr from ReserveRequest rr where rr.reserve =:rs and rr.reserveRequestStatus =:rrs" +
//                        " order by  rr.reserve.startTime   ");
//
//        q.setParameter("rs", reservation);
//        q.setParameter("rrs", status);
//
//        return (ArrayList<ReserveRequest>) q.list();

//        return createEntityCriteria()
//                .add(  Restrictions.like("barbershop" , barbershop) )
//                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
//                .list();


    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDao(){
        this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity) {
        getSession().persist(entity);
    }

    public void update(T entity) {
        getSession().update(entity);
    }
    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(persistentClass);
    }

    public List findAll(){
        return createEntityCriteria().setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }


}
