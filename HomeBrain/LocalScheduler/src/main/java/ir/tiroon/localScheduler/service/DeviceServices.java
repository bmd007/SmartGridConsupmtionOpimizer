package ir.tiroon.localScheduler.service;

import ir.tiroon.localScheduler.dao.DeviceDao;
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
public class DeviceServices {

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    UsageRecordDao usageRecordDao;

    public void addDevice(Device device) {
        deviceDao.persist(device);
    }

    public Device deviceByMac(String mac) {
        return deviceDao.deviceByMac(mac);
    }

    public void deleteDevice(Device device) {
        deviceDao.delete(device);
    }

    public ArrayList<Device> getDevices() {
        return (ArrayList<Device>) deviceDao.findAll();
    }

    public Device getDeviceById(long id) {
        return deviceDao.getByKey(id);
    }

    public void updateDevice(Device device) {
        deviceDao.update(device);
    }

    public void saveOrUpdate(Device device) {
        deviceDao.saveOrUpdate(device);
    }


    public ArrayList<UsageRecord> usageRecordsByDevice(Device device) {
        return usageRecordDao.usageRecordsByDevice(device);
    }

    public UsageRecord usageRecordByDataAndDevice(Device device, Date date) {
        return usageRecordDao.usageRecordByDataAndDevice(device, date);
    }


}
