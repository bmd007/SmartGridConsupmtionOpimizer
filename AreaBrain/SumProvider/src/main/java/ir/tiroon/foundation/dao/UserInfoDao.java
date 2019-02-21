package ir.tiroon.foundation.dao;

import ir.tiroon.foundation.model.UserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Neo BomBer on 12/11/2015.
 */


@Component
@Qualifier("userDao")
public class UserInfoDao extends AbstractDao<String,UserInfo> {}
