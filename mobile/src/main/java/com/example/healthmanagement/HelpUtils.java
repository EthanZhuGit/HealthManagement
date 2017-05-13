package com.example.healthmanagement;

import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.Record;
import com.example.healthmanagement.model.User;

import java.util.List;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class HelpUtils {
    public static final String BLOOD_PRESSURE = "blood_pressure";
    public static final String BLOOD_SUGAR = "blood_sugar";

    public static Record createBloodPressureRecord(List<BloodPressureItem> list) {
        return new Record(BLOOD_PRESSURE, list, null);
    }

    public static Record createBloodSugarRecord(List<BloodSugarItem> list) {
        return new Record(BLOOD_SUGAR, null, list);
    }


}
