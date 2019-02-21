package ir.tiroon.foundation.service;

import ir.tiroon.foundation.dao.UserInfoDao;
import ir.tiroon.foundation.model.UserInfo;
import ir.tiroon.foundation.paillier.jpaillier.KeyPair;
import ir.tiroon.foundation.paillier.jpaillier.KeyPairBuilder;
import ir.tiroon.foundation.paillier.jpaillier.PublicKey;
import ir.tiroon.foundation.util.JpaillierKeyProvider;
import ir.tiroon.foundation.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.*;

@Service("paillierService")
@Transactional
public class SumProvisionService {

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    JpaillierKeyProvider jpaillierKeyProvider;

    public BigInteger decrypt(BigInteger encryptedData) {
        return jpaillierKeyProvider.keyPair.decrypt(encryptedData);
    }

    public Future<BigInteger> asyncDecrypt(BigInteger encryptedData) {
        return new Future<BigInteger>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public BigInteger get() throws InterruptedException, ExecutionException {
                System.out.println("BMDD:decryption is in process");
                return jpaillierKeyProvider.keyPair.decrypt(encryptedData);
            }

            @Override
            public BigInteger get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    public BigInteger sumOverAllUserSchedulingRequestsSums() throws IOException, ClassNotFoundException {

        ArrayList<UserInfo> userInfos = (ArrayList<UserInfo>) userInfoDao.findAll();

        BigInteger encryptedSum = BigInteger.ONE;


        for (int i = 0; i < userInfos.size(); i++) {
            BigInteger temp =
                    (BigInteger) Util.fromString(userInfos.get(i).getLastEncryptedLocalSchedulingResult());
            encryptedSum = encryptedSum.multiply(temp).mod(jpaillierKeyProvider.keyPair.getPublicKey().getnSquared());
        }
        return encryptedSum;
    }

    public ArrayList<BigInteger> sumOverAllUsersSchedulingResultVectors() {
        ArrayList<BigInteger> encryptedSumVectorOfHoursOnArea = new ArrayList<>(24);
        ArrayList<UserInfo> userInfos = (ArrayList<UserInfo>) userInfoDao.findAll();

        final BigInteger[] encryptedSumOnAHour = {BigInteger.ONE};

        for (int i = 0; i < 24; i++) {
            int finalI = i;
            userInfos.stream().forEach(userInfo ->
                encryptedSumOnAHour[0] = encryptedSumOnAHour[0].multiply(
                        userInfo.getLastEncryptedLocalSumOfRequests().get(finalI)
                ).mod(jpaillierKeyProvider.keyPair.getPublicKey().getnSquared())
            );
            encryptedSumVectorOfHoursOnArea.add(i,encryptedSumOnAHour[0]);
        }
        return encryptedSumVectorOfHoursOnArea;
    }
}
