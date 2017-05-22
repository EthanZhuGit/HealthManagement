package com.example.healthmanagement.model;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/14 0014.
 */

public interface Record {

    String BLOOD_PRESSURE = "blood_pressure";
    String BLOOD_SUGAR = "blood_sugar";
    String HEART_RATE = "heart_rate";
    String BLOOD_OXYGEN = "blood_oxygen";

    String getName();

//    List<?> getItemList();
}
