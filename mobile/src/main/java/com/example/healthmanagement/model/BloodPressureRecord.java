package com.example.healthmanagement.model;

import android.util.Log;

import com.example.healthmanagement.datebase.LocalDateBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodPressureRecord implements Record {

    private static final String TAG = "TAG" + "BloodPressureRecord";
    private List<BloodPressureItem> bloodPressureItemList;

    private HashMap<Date, List<BloodPressureItem>> itemGroupedByCalendar;

    private List<BloodPressureItem> bloodPressureItemListForChart;


    public BloodPressureRecord(List<BloodPressureItem> ItemList) {

        this.bloodPressureItemList = ItemList;

        itemGroupedByCalendar = new HashMap<>();
        for (BloodPressureItem b :
                bloodPressureItemList) {
            Date d = b.getDate();
            Date startTimeOfDay = LocalDateBaseHelper.getStartTimeOfSpecifiedDaysAgo(d, 0);
            if (itemGroupedByCalendar.containsKey(startTimeOfDay)) {
                itemGroupedByCalendar.get(startTimeOfDay).add(b);
                Log.d(TAG, "BloodPressureRecord: " + "map contain  " + startTimeOfDay);
            } else {
                List<BloodPressureItem> l = new ArrayList<>();
                l.add(b);
                itemGroupedByCalendar.put(startTimeOfDay, l);
                Log.d(TAG, "BloodPressureRecord: " + "map not contain " + startTimeOfDay.getTime());
            }
        }

    }

    @Override
    public String getName() {
        return Record.BLOOD_PRESSURE;
    }

//    @Override
//    public List<BloodPressureItem> getItemList() {
//        return bloodPressureItemList;
//    }

    public HashMap<Date, List<BloodPressureItem>> getItemGroupedByCalendar() {
        return itemGroupedByCalendar;
    }

    public List<BloodPressureItem> getBloodPressureItemListForChart() {
        bloodPressureItemListForChart = new ArrayList<>();
        for (Map.Entry<Date, List<BloodPressureItem>> entry :
                itemGroupedByCalendar.entrySet()) {
            float low = 0f;
            float high = 0f;
            Date key = entry.getKey();
            List<BloodPressureItem> list = entry.getValue();
            for (BloodPressureItem b :
                    list) {
                low = low + b.getDiastolicPressure();
                high = high + b.getSystolicPressure();
            }
            BloodPressureItem item = new BloodPressureItem();
            item.setDate(key);
            item.setDiastolicPressure(low / list.size());
            item.setSystolicPressure(high / list.size());
            bloodPressureItemListForChart.add(item);
        }
        Collections.sort(bloodPressureItemListForChart, new Comparator<BloodPressureItem>() {
            @Override
            public int compare(BloodPressureItem o1, BloodPressureItem o2) {
                if (o1.getDate().after(o2.getDate())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return bloodPressureItemListForChart;
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
