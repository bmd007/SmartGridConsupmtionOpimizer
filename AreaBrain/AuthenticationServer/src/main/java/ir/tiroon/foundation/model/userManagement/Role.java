package ir.tiroon.foundation.model.userManagement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Role")
public class Role implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "roleId")
    long roleId;

    @Column(name = "name")
    String roleName;


    @Column(name = "description")
    String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<User> users = new HashSet<User>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission",
            joinColumns = {@JoinColumn(name = "roleId")},
            inverseJoinColumns = {@JoinColumn(name = "operationId")})
    @LazyCollection(LazyCollectionOption.FALSE)
    Set<Operation> operations = new HashSet<Operation>();

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public long getId() {
        return roleId;
    }

    public void setId(long id) {
        this.roleId = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
