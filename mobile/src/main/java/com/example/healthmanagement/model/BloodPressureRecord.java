package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodPressureRecord implements Record {

    private List<BloodPressureItem> bloodPressureItemList;

    public BloodPressureRecord(List<BloodPressureItem> bloodPressureItemList) {
        this.bloodPressureItemList = bloodPressureItemList;
    }

    @Override
    public String getName() {
        return Record.BLOOD_PRESSURE;
    }

    @Override
    public List<BloodPressureItem> getItemList() {
        return bloodPressureItemList;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BloodPressureRecord)) {
            return false;
        }
        BloodPressureRecord bloodPressureRecord = (BloodPressureRecord) obj;
        return this.getName().equals(bloodPressureRecord.getName());
    }
}
