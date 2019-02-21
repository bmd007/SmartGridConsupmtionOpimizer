package ir.tiroon.foundation.model.userManagement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Operation")
public class Operation implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "operationId")
    long operationId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    OperationType operationType;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "operations")
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<Role> assignedRoles = new HashSet<Role>();

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operation")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    Set<AgahiOperationLog> agahiOperationLogs = new HashSet<AgahiOperationLog>();

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }


    public Set<Role> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(Set<Role> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }

    public long getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public long getId() {
        return operationId;
    }

    public void setId(int id) {
        this.operationId = id;
    }

}
