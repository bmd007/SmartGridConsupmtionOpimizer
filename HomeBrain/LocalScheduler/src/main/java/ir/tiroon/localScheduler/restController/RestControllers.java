package ir.tiroon.localScheduler.restController;

import ir.tiroon.localScheduler.model.Device;
import ir.tiroon.localScheduler.model.UsageRecord;
import ir.tiroon.localScheduler.service.DeviceServices;
import ir.tiroon.localScheduler.service.UsageRecordServices;
import ir.tiroon.localScheduler.util.MQTTUtil2;
import ir.tiroon.localScheduler.util.RefreshTokenUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class RestControllers {


    @Autowired
    MQTTUtil2 mqttService;

    @Autowired
    DeviceServices deviceServices;

    @Autowired
    UsageRecordServices usageRecordServices;

    @RequestMapping(value = "/tellRefreshToken/{rt}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity tellRefreshToken(@PathVariable("rt") String refreshToken) throws Exception {
        System.out.println("BMD:refresh token"+refreshToken);
        RefreshTokenUtil.setRefreshToken(refreshToken);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/addDevice", method = RequestMethod.POST, consumes = "application/json; charset=utf-8;")
    @ResponseBody
    public ResponseEntity<Device> addDevice(@RequestBody Device device) throws Exception {

        Device dev = deviceServices.deviceByMac(device.getMac());

        if (dev == null)
            deviceServices.addDevice(device);
        else
            return new ResponseEntity<>(dev, HttpStatus.CONFLICT);

        return new ResponseEntity<>(device, HttpStatus.OK);
    }


    @RequestMapping(value = "/deleteDevice/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Device> deleteDevice(@PathVariable("id") Long id) throws Exception {
        Device dv = deviceServices.getDeviceById(id);
        deviceServices.deleteDevice(dv);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/deviceList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ArrayList<Device>> deviceList() throws Exception {
        return new ResponseEntity<>(deviceServices.getDevices(), HttpStatus.OK);
    }

    @RequestMapping(value = "/usageRequestList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<ArrayList<UsageRecord>> usageList() throws Exception {
        return new ResponseEntity<>(usageRecordServices.getUsageRecords(), HttpStatus.OK);
    }

    @RequestMapping(value = "/device/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Device> deviceById(@PathVariable("id") long id) throws Exception {
        return new ResponseEntity<>(deviceServices.getDeviceById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/tellRequestVector", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<UsageRecord> advertiseUsage(@RequestBody String uRecord) throws Exception {

        JSONObject jsonObject = new JSONObject(uRecord);

        Date usageRecordRequestDate = new Date(jsonObject.getLong("date"));

//        if (checkDateForUsageRequests(usageRecordRequestDate))
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        else {

            Device device = deviceServices.getDeviceById(jsonObject.getJSONObject("device").getLong("id"));
            UsageRecord usageRecord = deviceServices.usageRecordByDataAndDevice(device , usageRecordRequestDate);

            if (usageRecord == null) {
                usageRecord = new UsageRecord(new Date((Long) jsonObject.get("date")), device);

                List<Integer> ints = new ArrayList<>(24);
                for(int i=0; i<24;i++)ints.add(0);
                usageRecord.setScheduledVector(ints);
            }

            List<Integer> ints = new ArrayList<>(24);
            jsonObject.getJSONArray("requestVector").toList().stream().forEach(o -> ints.add((Integer) o));

            usageRecord.setRequestVector(ints);

            usageRecordServices.saveOrUpdate(usageRecord);

            return new ResponseEntity<>(usageRecord, HttpStatus.OK);
//        }
    }


    boolean checkDateForUsageRequests(Date theDay) {

        java.util.Date today = new java.util.Date();

        boolean b1 = today.compareTo(theDay) >= 0; //today is >= theDay

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        boolean b2 = calendar.get(Calendar.HOUR_OF_DAY) > 13;//current Hour is after 13
        calendar.add(Calendar.DATE,1);
        boolean b3 = theDay.compareTo(calendar.getTime()) == 0; //the day is tomorrow

        return !( b1 || (b2 && b3) );
    }

}
