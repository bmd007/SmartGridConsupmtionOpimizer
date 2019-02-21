package ir.tiroon.localScheduler.service;

import ir.tiroon.localScheduler.dao.UsageRecordDao;
import ir.tiroon.localScheduler.model.Device;
import ir.tiroon.localScheduler.model.UsageRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;

@Transactional
@Service
public class UsageRecordServices {

    @Autowired
    UsageRecordDao usageRecordDao;

    public void saveOrUpdate(UsageRecord usageRecord){
        usageRecordDao.saveOrUpdate(usageRecord);
    }

    public void addUsageRecord(UsageRecord usageRecord){
        usageRecordDao.persist(usageRecord);
    }

    public void deleteUsageRecord(UsageRecord usageRecord){
        usageRecordDao.delete(usageRecord);
    }

    public ArrayList<UsageRecord> getUsageRecords(){
        return (ArrayList<UsageRecord>) usageRecordDao.findAll();
    }

    public ArrayList<UsageRecord> getUsageRecordsByDate(Date date){
        return usageRecordDao.usageRecordsByData(date);
    }

}
