package ir.tiroon.localScheduler.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UsageRecord")
public class UsageRecord implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    long usageRecordId;

    @Column(nullable = false)
    Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deviceId", nullable = false)
    Device device;

    @Column
    @Convert(converter = IntArrayToStringConverter.class)
    List<Integer> requestVector = new ArrayList<>(24);

    @Column
    @Convert(converter = IntArrayToStringConverter.class)
    List<Integer> scheduledVector = new ArrayList<>(24);

    public UsageRecord() {
    }

    @JsonCreator
    public UsageRecord(@JsonProperty("date") Date date, @JsonProperty("device") Device device) {
        this.date = date;
        this.device = device;
    }

    public long getUsageRecordId() {
        return usageRecordId;
    }

    public void setUsageRecordId(long usageRecordId) {
        this.usageRecordId = usageRecordId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<Integer> getRequestVector() {
        return requestVector;
    }

    public void setRequestVector(List<Integer> requestVector) {
        this.requestVector = requestVector;
    }

    public List<Integer> getScheduledVector() {
        return scheduledVector;
    }

    public void setScheduledVector(List<Integer> scheduledVector) {
        this.scheduledVector = scheduledVector;
    }

    @Override
    public String toString() {
        return "Date::"+date.toString()+" Device ID:"+device.getDeviceId()+" ::"+requestVector.get(0);
    }
}
