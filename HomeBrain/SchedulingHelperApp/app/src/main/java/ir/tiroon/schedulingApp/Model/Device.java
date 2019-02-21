package ir.tiroon.schedulingApp.Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends SugarRecord implements Serializable {

    String name;

    @Unique
    String mac;

    boolean automatic = false;

    double usagePerHour;

//    @JsonIgnore
//    Set<UsageRecord> usageRecords = new HashSet<>();

    @JsonCreator
    public Device(@JsonProperty("deviceId") Long deviceId, @JsonProperty("name") String name,@JsonProperty("mac") String mac,@JsonProperty("automatic") boolean automatic,
                  @JsonProperty("usagePerHour") double usagePerHour) {
        super.setId(deviceId);
        this.name = name;
        this.mac = mac;
        this.automatic = automatic;
        this.usagePerHour = usagePerHour;
    }



    public Device() {
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

    //    public Set<UsageRecord> getUsageRecords() {
//        return usageRecords;
//    }
//
//    public void setUsageRecords(Set<UsageRecord> usageRecords) {
//        this.usageRecords = usageRecords;
//    }
}
