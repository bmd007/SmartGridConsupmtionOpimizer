package ir.tiroon.foundation.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Table(name = "UserInfo")
@Inheritance(strategy=InheritanceType.JOINED)
public class UserInfo implements Serializable {

    public UserInfo(){}

    @Id
    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false , unique = true)
    String postAddress;

    @Column(length = 2048)
    String lastEncryptedLocalSchedulingResult;

    @Column(length = 100000)
    @Convert(converter = BigIntegerArrayToByteConverter.class)
    List<BigInteger> lastEncryptedLocalSumOfRequests = new ArrayList<>(24);


    public UserInfo(String email, String postAddress) {
        this.email = checkNotNull(email);
        this.postAddress = checkNotNull(postAddress);
    }

    public List<BigInteger> getLastEncryptedLocalSumOfRequests() {
        return lastEncryptedLocalSumOfRequests;
    }

    public void setLastEncryptedLocalSumOfRequests(List<BigInteger> lastEncryptedLocalSumOfRequests) {
        this.lastEncryptedLocalSumOfRequests = lastEncryptedLocalSumOfRequests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }


    public String getLastEncryptedLocalSchedulingResult() {
        return lastEncryptedLocalSchedulingResult;
    }

    public void setLastEncryptedLocalSchedulingResult(String lastEncryptedLocalSchedulingResult) {
        this.lastEncryptedLocalSchedulingResult = lastEncryptedLocalSchedulingResult;
    }
}
