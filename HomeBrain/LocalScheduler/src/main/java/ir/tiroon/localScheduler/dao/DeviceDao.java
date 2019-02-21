package ir.tiroon.localScheduler.dao;

import ir.tiroon.localScheduler.model.Device;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceDao extends AbstractDao<Long,Device> {

    public Device deviceByMac(String mac){
        List list = createEntityCriteria()
                .add(Restrictions.like("mac", mac))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        if (list.size()>0)
            return (Device) list.get(0);
        else
            return null;
    }

}
