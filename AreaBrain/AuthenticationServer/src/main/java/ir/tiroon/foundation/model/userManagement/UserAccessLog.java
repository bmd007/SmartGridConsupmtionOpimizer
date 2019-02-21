package ir.tiroon.foundation.model.userManagement;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "UserAccessLog")
public class UserAccessLog implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "accessLogId")
    long accessLogId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    User user;

    @Column(name = "date")
    Date date;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    AccessLogType accessLogType;

    public AccessLogType getAccessLogType() {
        return accessLogType;
    }

    public void setAccessLogType(AccessLogType accessLogType) {
        this.accessLogType = accessLogType;
    }

    public long getId() {
        return accessLogId;
    }

    public void setId(long id) {
        this.accessLogId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
