package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class HeartRateRecord implements Record {
    private List<HeartRateItem> heartRateItemList;

    public HeartRateRecord(List<HeartRateItem> heartRateItemList) {
        this.heartRateItemList = heartRateItemList;
    }

    @Override
    public String getName() {
        return Record.HEART_RATE;
    }

    @Override
    public List<HeartRateItem> getItemList() {

        return heartRateItemList;
    }
}
