package com.example.healthmanagement.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/15 0015.
 */

public class BloodSugarRecord implements Record {

    private List<BloodSugarItem> bloodSugarItemList;


    public BloodSugarRecord(List<BloodSugarItem> ItemList) {
        this.bloodSugarItemList = ItemList;

    }

    public List<BloodSugarItemForChart> getBloodSugarItemForChartList() {
        List<BloodSugarItemForChart> bloodSugarItemForChartList = new ArrayList<>();

        if (bloodSugarItemList != null) {
            for (int i = 0; i < bloodSugarItemList.size(); i++) {
                BloodSugarItem item = bloodSugarItemList.get(i);
                float sumBefore=0;
                int count=0;
                if (item.getBeforeBreakfast() != 0) {
                    sumBefore=sumBefore+item.getBeforeBreakfast();
                    count++;
                }
                if (item.getBeforeLunch() != 0) {
                    sumBefore=sumBefore+item.getBeforeLunch();
                    count++;
                }
                if (item.getBeforeSupper() != 0) {
                    sumBefore=sumBefore+item.getBeforeSupper();
                    count++;
                }
                float beforeMealAvg = 0;
                if (count!=0) {
                    beforeMealAvg = Float.valueOf(
                            new DecimalFormat("###.#").format(
                                    (sumBefore / count)));
                }

                float sumAfter=0;
                count=0;
                if (item.getAfterBreakfast() != 0) {
                    sumAfter = sumAfter + item.getAfterBreakfast();
                    count++;
                }
                if (item.getAfterLunch() != 0) {
                    sumAfter = sumAfter + item.getAfterLunch();
                    count++;
                }
                if (item.getAfterSupper() != 0) {
                    sumAfter = sumAfter + item.getAfterSupper();
                    count++;
                }
                float afterMealAvg=0;
                if (count != 0) {
                    afterMealAvg = Float.valueOf(
                            new DecimalFormat("###.#").format(
                                    (sumAfter / count)));
                }

                BloodSugarItemForChart bloodSugarItemForChart = new BloodSugarItemForChart(item.getDate(), beforeMealAvg, afterMealAvg, item.getBeforeDawn(), item.getBeforeSleep());
                bloodSugarItemForChartList.add(bloodSugarItemForChart);
            }
        }
        return bloodSugarItemForChartList;
    }

    @Override
    public String getName() {
        return Record.BLOOD_SUGAR;
    }

//    @Override
//    public List<BloodSugarItem> getItemList() {
//        return bloodSugarItemList;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BloodSugarRecord)) {
            return false;
        }
        BloodSugarRecord BloodSugarRecord = (BloodSugarRecord) obj;
        return this.getName().equals(BloodSugarRecord.getName());
    }

    public List<BloodSugarItem> getBloodSugarItemList() {
        return bloodSugarItemList;
    }
}
