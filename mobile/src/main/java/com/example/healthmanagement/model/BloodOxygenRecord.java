package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodOxygenRecord implements Record {
    private List<BloodOxygenItem> bloodOxygenItemList;

    public BloodOxygenRecord(List<BloodOxygenItem> bloodOxygenItemList) {
        this.bloodOxygenItemList = bloodOxygenItemList;
    }

    @Override
    public String getName() {
        return Record.BLOOD_OXYGEN;
    }

    @Override
    public List<BloodOxygenItem> getItemList() {
        return bloodOxygenItemList;
    }
}