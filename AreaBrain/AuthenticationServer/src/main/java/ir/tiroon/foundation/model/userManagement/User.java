package ir.tiroon.foundation.model.userManagement;


import com.fasterxml.jackson.annotation.JsonCreator;
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
@Table(name = "User")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements Serializable {

    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", unique = true, nullable = false, updatable = true)
    long userId;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(nullable = false )//, unique = true)
    String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")})
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<Role> roles = new HashSet<Role>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<UserAccessLog> accessLogs = new HashSet<UserAccessLog>();


    @Column(name="State", nullable=false)
    @Enumerated(EnumType.STRING)
    State state=State.Active;

    @Override
    public int hashCode() {
        final int prime = 31;
        long result = 1;
        result = prime * result + userId;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return (int) result;
    }

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("name") String name,@JsonProperty("password") String password,
                @JsonProperty("email") String email,@JsonProperty("phoneNumber") String phone) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<UserAccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(Set<UserAccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    public long getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String toString() {
        return userId + "::" + email + "::" + name + "::" + password + "::";
    }
}
