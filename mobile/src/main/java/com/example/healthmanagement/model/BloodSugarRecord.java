package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodSugarRecord implements Record {

    private List<BloodSugarItem> bloodSugarItemList;

    public BloodSugarRecord(List<BloodSugarItem> bloodSugarItemList) {
        this.bloodSugarItemList = bloodSugarItemList;
    }

    @Override
    public String getName() {
        return Record.BLOOD_SUGAR;
    }

    @Override
    public List<BloodSugarItem> getItemList() {
        return bloodSugarItemList;
    }
}
