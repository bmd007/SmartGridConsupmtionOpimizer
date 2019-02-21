package ir.tiroon.localScheduler.dao;

import ir.tiroon.localScheduler.model.Device;
import ir.tiroon.localScheduler.model.UsageRecord;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsageRecordDao extends AbstractDao<Date,UsageRecord> {

    public ArrayList<UsageRecord> usageRecordsByDevice(Device device){

        return (ArrayList<UsageRecord>) createEntityCriteria().add(Restrictions.like("device",device)).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();

    }

    public UsageRecord usageRecordByDataAndDevice(Device device, Date date){
        List list = createEntityCriteria()
                .add(Restrictions.like("device", device))
                .add(Restrictions.like("date", date))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        if (list.size()>0)
            return (UsageRecord) list.get(0);
        else
            return null;
    }

    public ArrayList<UsageRecord> usageRecordsByData(Date date){
        return (ArrayList<UsageRecord>) createEntityCriteria()
                .add(Restrictions.like("date", date))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();

    }

}
