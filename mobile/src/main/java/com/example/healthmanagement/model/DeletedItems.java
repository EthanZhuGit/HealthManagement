package com.example.healthmanagement.model;

import org.litepal.crud.DataSupport;


/**
 * Created by zyx10 on 2017/5/21 0021.
 */

public class DeletedItems extends DataSupport {

    private int id;
    private String tableNameOfItem;
    private String objectIdOfItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableNameOfItem() {
        return tableNameOfItem;
    }

    public void setTableNameOfItem(String tableNameOfItem) {
        this.tableNameOfItem = tableNameOfItem;
    }

    public String getObjectIdOfItem() {
        return objectIdOfItem;
    }

    public void setObjectIdOfItem(String objectIdOfItem) {
        this.objectIdOfItem = objectIdOfItem;
    }
}
