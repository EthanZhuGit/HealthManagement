package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodSugarRecord implements Record {

    private List<BloodSugarItem> bloodSugarItemList;

    public BloodSugarRecord(List<BloodSugarItem> ItemList) {
        this.bloodSugarItemList = ItemList;

    }

    @Override
    public String getName() {
        return Record.BLOOD_SUGAR;
    }

//    @Override
//    public List<BloodSugarItem> getItemList() {
//        return bloodSugarItemList;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BloodSugarRecord)) {
            return false;
        }
        BloodSugarRecord BloodSugarRecord = (BloodSugarRecord) obj;
        return this.getName().equals(BloodSugarRecord.getName());
    }
}
