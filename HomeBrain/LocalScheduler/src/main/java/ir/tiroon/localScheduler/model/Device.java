package ir.tiroon.localScheduler.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Device")
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    long deviceId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false,unique = true)
    String mac;

    @Column(nullable = false )
    boolean automatic = false;

    @Column(nullable = false)
    double usagePerHour;


    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<UsageRecord> usageRecords = new HashSet<>();

    @JsonCreator
    public Device(@JsonProperty("name") String name,@JsonProperty("mac") String mac,@JsonProperty("automatic") boolean automatic,
                  @JsonProperty("usagePerHour") double usagePerHour) {
        this.name = name;
        this.mac = mac;
        this.automatic = automatic;
        this.usagePerHour = usagePerHour;
    }


    public Device() {
    }


    public boolean isAutomatic() {
        return automatic;
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

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
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

    public Set<UsageRecord> getUsageRecords() {
        return usageRecords;
    }

    public void setUsageRecords(Set<UsageRecord> usageRecords) {
        this.usageRecords = usageRecords;
    }
}
