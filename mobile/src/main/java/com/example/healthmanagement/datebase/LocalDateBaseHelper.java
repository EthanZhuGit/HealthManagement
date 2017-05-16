package com.example.healthmanagement.datebase;

import android.util.Log;

import com.example.healthmanagement.model.BloodOxygenItem;
import com.example.healthmanagement.model.BloodOxygenRecord;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.BloodSugarRecord;
import com.example.healthmanagement.model.HeartRateItem;
import com.example.healthmanagement.model.HeartRateRecord;
import com.example.healthmanagement.model.User;

import org.litepal.crud.DataSupport;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class LocalDateBaseHelper {
    private static final String TAG = "TAG" + "LocalDateBaseHelper";

    /*
     * 获取指定天数之前的开始时间
     * 今天是5月13号任何时间，则返回 5月(13-i)号 00:00:00
     */
    private static Date getDaysBeforeStartTime(int i) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) - i,
                0, 0, 0);
        return calendar1.getTime();
    }

    public static BloodOxygenRecord getBloodOxygenRecord() {
        return new BloodOxygenRecord(getRecord(BloodOxygenItem.class));
    }

    public static BloodPressureRecord getBloodPressureRecord() {
        return new BloodPressureRecord(getRecord(BloodPressureItem.class));
    }

    public static BloodSugarRecord getBloodSugarRecord() {
        return new BloodSugarRecord(getRecord(BloodSugarItem.class));
    }
    public static HeartRateRecord getHeartRateRecord() {
        return new HeartRateRecord(getRecord(HeartRateItem.class));
    }

    private static <T> List<T> getRecord(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        Date date = new Date();
        int daysWithRecord = 0;
        int queryDays=0;
        long startDate;
        long endDate;
        int oldListSize;
        int totalNum = DataSupport.count(tClass);
        while (daysWithRecord <= 6) {
            endDate=date.getTime();
            startDate = getDaysBeforeStartTime(queryDays).getTime();
            oldListSize = list.size();
            list = DataSupport.where("date between ? and ?", String.valueOf(startDate), String.valueOf(endDate)).find(tClass);
            if (list.size() > oldListSize) {
                daysWithRecord++;
            }
            queryDays++;
            if (list.size() == totalNum) {
                break;
            }
        }
        Log.d(TAG, "getRecord: " + tClass.getSimpleName() + " " + list.size());
        return list;
    }

    /**
     * @param uid               用户id
     * @param date              日期
     * @param systolicPressure  收缩压
     * @param diastolicPressure 舒张压
     * @param lastModifyDate    最后更新时间
     */
    public static void saveBloodPressureItem(String uid, Date date, float systolicPressure, float diastolicPressure, Date lastModifyDate) {
        List<User> users = DataSupport.where("phonenum=?", uid).find(User.class);
        User user = users.get(0);
        List<BloodPressureItem> bloodPressureItemList = user.getBloodPressureItemList();
        BloodPressureItem bloodPressureItem = new BloodPressureItem();
        bloodPressureItem.setDate(date);
        bloodPressureItem.setSystolicPressure(systolicPressure);
        bloodPressureItem.setDiastolicPressure(diastolicPressure);
        bloodPressureItem.setLastModifyDate(lastModifyDate);
        bloodPressureItem.save();
        bloodPressureItemList.add(bloodPressureItem);
        user.saveOrUpdate("phonenum=?", uid);
    }

    public static void saveBloodPressureItem(String uid, BloodPressureItem item) {
        List<User> users = DataSupport.where("phonenum=?", uid).find(User.class);
        User user = users.get(0);
        List<BloodPressureItem> bloodPressureItemList = user.getBloodPressureItemList();
        item.save();
        bloodPressureItemList.add(item);
        user.saveOrUpdate("phonenum=?", uid);
    }

}
