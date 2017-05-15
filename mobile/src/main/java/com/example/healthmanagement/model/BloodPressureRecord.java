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
}
