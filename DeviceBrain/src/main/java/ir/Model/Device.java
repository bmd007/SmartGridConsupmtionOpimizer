package ir.Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    long deviceId;

    String name;

    String mac;

    boolean automatic = false;

    double usagePerHour;

    @JsonIgnore
    Set<UsageRecord> usageRecords = new HashSet<UsageRecord>();

    @JsonCreator
    public Device(@JsonProperty("name") String name, @JsonProperty("mac") String mac, @JsonProperty("automatic") boolean automatic,
                  @JsonProperty("usagePerHour") double usagePerHour) {
        this.name = name;
        this.mac = mac;
        this.automatic = automatic;
        this.usagePerHour = usagePerHour;
    }


    public Device() {
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public double getUsagePerHour() {
        return usagePerHour;
    }

    public void setUsagePerHour(double usagePerHour) {
        this.usagePerHour = usagePerHour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public Set<UsageRecord> getUsageRecords() {
        return usageRecords;
    }

    public void setUsageRecords(Set<UsageRecord> usageRecords) {
        this.usageRecords = usageRecords;
    }
}
