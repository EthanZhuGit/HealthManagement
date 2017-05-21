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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class LocalDateBaseHelper {
    private static final String TAG = "TAG" + "LocalDateBaseHelper";

    /*
     * 获取从现在开始指定天数之前的开始时间
     * 今天是5月13号任何时间，则返回 5月(13-i)号 00:00:00
     */
    public static Date getStartTimeOfSpecifiedDaysAgo(int i) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) - i,
                0, 0, 0);

        return calendar1.getTime();
    }

    public static Date getStartTimeOfSpecifiedDaysAgo(Date date, int i) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH) - i,
                0, 0, 0);
        return calendar1.getTime();
    }

    public static BloodOxygenRecord getBloodOxygenRecord(String user_id) {
        return new BloodOxygenRecord(getRecord(user_id, BloodOxygenItem.class));
    }
    public static BloodOxygenRecord getAllBloodOxygenRecord(String user_id) {
        return new BloodOxygenRecord(DataSupport.where("user_id=?",user_id).order("date asc").find(BloodOxygenItem.class));
    }


    public static BloodPressureRecord getBloodPressureRecord(String user_id) {
        Log.d(TAG, "getBloodPressureRecord: ");
        return new BloodPressureRecord(getRecord(user_id, BloodPressureItem.class));
    }
    public static BloodPressureRecord getAllBloodPressureData(String  user_id) {

        return new BloodPressureRecord(DataSupport.where("user_id=?",user_id).order("date asc").find(BloodPressureItem.class));
    }


    public static BloodSugarRecord getBloodSugarRecord(String user_id) {
        return new BloodSugarRecord(getRecord(user_id, BloodSugarItem.class));
    }
    public static BloodSugarRecord getAllBloodSugarData(String user_id) {
//        return new BloodSugarRecord(DataSupport.findAll(BloodSugarItem.class));
        return new BloodSugarRecord(DataSupport.where("user_id=?",user_id).order("date asc").find(BloodSugarItem.class));
    }


    public static HeartRateRecord getHeartRateRecord(String user_id) {
        return new HeartRateRecord(getRecord(user_id, HeartRateItem.class));
    }
    public static HeartRateRecord getAllHeartRateData(String user_id) {
        return new HeartRateRecord(DataSupport.where("user_id=?",user_id).order("date asc").find(HeartRateItem.class));
    }

    /**
     * 获取7天的记录
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T extends DataSupport> List<T> getRecord(String user_id, Class<T> tClass ) {
        List<T> list = new ArrayList<>();
        Date date = new Date();
        int daysWithRecord = 0;
        int queryDays=0;
        long startDate;
        long endDate;
        int oldListSize;
        int totalNum = DataSupport.where("user_id=?",user_id).count(tClass);
        Log.d(TAG, "getRecord: " + totalNum);
        while (daysWithRecord <= 6) {
            endDate=date.getTime();
            startDate = getStartTimeOfSpecifiedDaysAgo(queryDays).getTime();
            oldListSize = list.size();
            list = DataSupport.where("date between ? and ? and user_id=?", String.valueOf(startDate), String.valueOf(endDate),user_id).order("date asc").find(tClass);
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
     * 获取指定某一天的记录
     * @param tClass
     * @param date 指定某一天内的任意时刻Date对象
     * @param <T>
     * @return
     */
    public static <T> List<T> getRecordInOneSpecifiedDay(Class<T> tClass, Date date) {
        long startTime = getStartTimeOfSpecifiedDaysAgo(date, 0).getTime();
        long endTime = getStartTimeOfSpecifiedDaysAgo(date, -1).getTime();
        return  DataSupport.where("date >= ? and < ?", String.valueOf(startTime), String.valueOf(endTime)).find(tClass);
    }

    public static <T> List<T> getRecord(Class<T> tClass, Date date, int days) {
        return null;
    }

    /**
     * @param uid               用户id
     * @param date              日期
     * @param systolicPressure  收缩压
     * @param diastolicPressure 舒张压
     * @param lastModifyDate    最后更新时间
     */
    public static void saveBloodPressureItem(String uid, Date date, float systolicPressure, float diastolicPressure, Date lastModifyDate) {
        List<User> users = DataSupport.where("id=?", uid).find(User.class);
        User user = users.get(0);
        List<BloodPressureItem> bloodPressureItemList = user.getBloodPressureItemList();
        BloodPressureItem bloodPressureItem = new BloodPressureItem();
        bloodPressureItem.setDate(date);
        bloodPressureItem.setSystolicPressure(systolicPressure);
        bloodPressureItem.setDiastolicPressure(diastolicPressure);
        bloodPressureItem.setLastModifyDate(lastModifyDate);
        bloodPressureItem.save();
        bloodPressureItemList.add(bloodPressureItem);
        user.saveOrUpdate("id=?", uid);
    }

    public static void saveBloodPressureItem(String uid, BloodPressureItem item) {
        List<User> users = DataSupport.where("id=?", uid).find(User.class);
        User user = users.get(0);
        Log.d(TAG, "saveBloodPressureItem: " + user.getUsername() + user.getObject_id() + " ");

//        user.getBloodPressureItemList().add(item);
//        if (user.saveOrUpdate("id=?", uid)) {
//            Log.d(TAG, "saveBloodPressureItem: " + "save suc");
//        }
//        user.save();
        item.setUser(user);
        if (item.save()) {
            Log.d(TAG, "saveBloodPressureItem: " + "suc");
        } else {
            Log.d(TAG, "saveBloodPressureItem: " + "fail");
        }

    }

//    public static  String getUserIdWithObjectId(String objectId) {
//        List<User> users = DataSupport.where("object_id=?", objectId).find(User.class);
//        User user = users.get(0);
//        return String.valueOf(user.getId());
//    }


    public static String getUserId(String object_id) {
        List<User> users = DataSupport.where("object_id=?", object_id).find(User.class);
        return String.valueOf(users.get(0).getId());
    }

}
