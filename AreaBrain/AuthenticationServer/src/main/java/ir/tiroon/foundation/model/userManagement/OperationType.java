package ir.tiroon.foundation.model.userManagement;

import java.io.Serializable;

/**
 * Created by Neo BomBer on 12/21/2015.
 */
public enum OperationType implements Serializable {

    ReadNode, CreateNode, DeleteNode, UpdateNode,
    CreateUser, EditUser, DeleteUser,
    PublishNode, UnPublishNode,
    Like, disLike
}
