package ir.tiroon.foundation.service.userManagement;

import ir.tiroon.foundation.dao.userManagement.OperationDao;
import ir.tiroon.foundation.model.userManagement.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service("operationService")
@Transactional
public class OperationServices {

    @Autowired
    OperationDao operationDao;


    public OperationDao getOperationDao() {
        return operationDao;
    }

    public void setOperationDao(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    public long addEntity(Operation employee) {
        return operationDao.persist(employee);
    }

    public Operation getEntityById(int id) {
        return operationDao.findById(id);
    }

    public ArrayList<Operation> getEntityList() {
        return operationDao.findAll();
    }

    public void updateOperation(Operation operation) {
        operationDao.saveOrUpdate(operation);
    }

    public boolean deleteEntity(int id) {
        operationDao.delete(operationDao.findById(id));
        return true;
    }

}