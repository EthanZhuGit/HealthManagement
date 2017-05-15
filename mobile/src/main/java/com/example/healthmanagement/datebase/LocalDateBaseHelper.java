package com.example.healthmanagement.datebase;

import com.example.healthmanagement.model.BloodOxygenItem;
import com.example.healthmanagement.model.BloodOxygenRecord;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.BloodSugarRecord;
import com.example.healthmanagement.model.HeartRateItem;
import com.example.healthmanagement.model.HeartRateRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class LocalDateBaseHelper {




    /**
     * 获取七天之前的开始时间
     * 今天是5月13号任何时间，则返回5月7号 00:00:00
     * @return
     */
    public static Date getSenvenDaysBeforStartTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)-6,
                0, 0, 0);
        return calendar1.getTime();
    }

    public static BloodOxygenRecord getOneWeekBloodOxygenRecord() {
        List<BloodOxygenItem> bloodOxygenItemList = new ArrayList<>();
        return new BloodOxygenRecord(bloodOxygenItemList);
    }

    public static BloodPressureRecord getOnWeekBloodPressureRecord() {
        List<BloodPressureItem> bloodPressureItemList = new ArrayList<>();
        return new BloodPressureRecord(bloodPressureItemList);
    }

    public static BloodSugarRecord getOneWeekBloodSugarRecord() {
        List<BloodSugarItem> bloodSugarItemList = new ArrayList<>();
        return new BloodSugarRecord(bloodSugarItemList);
    }

    public static HeartRateRecord getOneWeekHeartRateRecord() {
        List<HeartRateItem> heartRateItemList = new ArrayList<>();
        return new HeartRateRecord(heartRateItemList);
    }
}
