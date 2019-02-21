package ir.tiroon.schedulingApp.Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsageRecord extends SugarRecord implements Serializable {

    long date;

    Device device;

    String requestVector;

    String scheduledVector;

    public UsageRecord() {
    }

//    @JsonCreator
    public UsageRecord(@JsonProperty("date") Date date, @JsonProperty("device") Device device) {
        this.date = date.getTime();
        this.device = device;
    }

    @JsonCreator
    public UsageRecord(@JsonProperty("usageRecordId") Long usageRecordId,
                       @JsonProperty("date") Date date, @JsonProperty("device") Device device,
                       @JsonProperty("requestVector") List<Integer> requestVector,
                       @JsonProperty("scheduledVector") List<Integer> scheduledVector) {
        super.setId(usageRecordId);
        this.date = date.getTime();
        this.device = device;
        this.requestVector = requestVector == null ? null : StringUtils.join(",", requestVector);
        this.scheduledVector = scheduledVector == null ? null : StringUtils.join(",", scheduledVector);
    }

    public Date getDate() {
        return new java.sql.Date(date);
    }

    public long getDateInTimes(){return date;}

    public void setDate(Date date) {
        this.date = date.getTime();
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<Integer> getRequestVector() {

        List<Integer> integers = new ArrayList<>(24);

        String[] split = requestVector.substring(2, requestVector.length() - 1).split(",");
        for (String s: split)
            integers.add(Integer.parseInt(s.trim()));

        return integers;
    }

    public void setRequestVector(List<Integer> requestVector) {
        this.requestVector = requestVector == null ? null : StringUtils.join(",", requestVector);
    }

    public List<Integer> getScheduledVector() {
        List<Integer> integers = new ArrayList<>(24);

        String[] split = scheduledVector.substring(2, scheduledVector.length() - 1).split(",");
        for (String s: split)
            integers.add(Integer.parseInt(s.trim()));

        return integers;
    }

    public void setScheduledVector(List<Integer> scheduledVector) {
        this.scheduledVector = scheduledVector == null ? null : StringUtils.join(",", scheduledVector);
    }

    public String toString(){
        return getId()+":"+device.getId()+":"+new Date(date)+":"+requestVector;
    }
}
