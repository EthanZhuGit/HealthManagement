package com.example.healthmanagement.model;

import com.example.healthmanagement.datebase.LocalDateBaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class HeartRateRecord implements Record {
    private List<HeartRateItem> heartRateItemList;

    private HashMap<Date,List<HeartRateItem>> itemGroupedByCalendar;

    private List<HeartRateItem> heartRateItemListForChart;

    public HeartRateRecord(List<HeartRateItem> heartRateItemList) {
        this.heartRateItemList = heartRateItemList;
        itemGroupedByCalendar = new HashMap<>();
        for (HeartRateItem item : heartRateItemList) {
            Date d = item.getDate();
            Date startTimeOfDay = LocalDateBaseHelper.getStartTimeOfSpecifiedDaysAgo(d, 0);
            if (itemGroupedByCalendar.containsKey(startTimeOfDay)) {
                itemGroupedByCalendar.get(startTimeOfDay).add(item);
            } else {
                List<HeartRateItem> l = new ArrayList<>();
                l.add(item);
                itemGroupedByCalendar.put(startTimeOfDay, l);
            }
        }
    }

    @Override
    public String getName() {
        return Record.HEART_RATE;
    }

    public HashMap<Date, List<HeartRateItem>> getItemGroupedByCalendar() {
        return itemGroupedByCalendar;
    }

    public List<HeartRateItem> getHeartRateItemListForCHart() {
        heartRateItemListForChart=new ArrayList<>();
        for (Map.Entry<Date, List<HeartRateItem>> entry :
                itemGroupedByCalendar.entrySet()) {
            int rate = 0;
            Date key = entry.getKey();
            List<HeartRateItem> list = entry.getValue();
            for (HeartRateItem i :
                    list) {
                rate = rate + i.getRate();
            }
            HeartRateItem item = new HeartRateItem();
            item.setDate(key);
            int rateAvg = rate / list.size();
            item.setRate(rateAvg);
            heartRateItemListForChart.add(item);
        }
        Collections.sort(heartRateItemListForChart, new Comparator<HeartRateItem>() {
            @Override
            public int compare(HeartRateItem o1, HeartRateItem o2) {
                if (o1.getDate().after(o2.getDate())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return heartRateItemListForChart;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HeartRateRecord)) {
            return false;
        }
        HeartRateRecord heartRateRecord = (HeartRateRecord) obj;
        return this.getName().equals(heartRateRecord.getName());
    }
}
