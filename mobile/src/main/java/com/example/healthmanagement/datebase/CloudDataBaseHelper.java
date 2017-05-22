package com.example.healthmanagement.datebase;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.DeletedItems;
import com.example.healthmanagement.model.HeartRateItem;
import com.example.healthmanagement.model.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class CloudDataBaseHelper {
    private static final String TAG = "TAG" + "CloudDataBaseHelper";

    public static void upLoad() {
        upLoadBloodPressure();
        upLoadBloodSugar();
        upLoadHeartRate();
        deleteItemsFromCloud();
    }



    private static void upLoadBloodPressure() {
        final List<BloodPressureItem> bloodPressureItemList = DataSupport.where("iscloudstorage=?", "0").find(BloodPressureItem.class, true);
        Log.d(TAG, "upLoadBloodPressure: " + bloodPressureItemList.size());
        final List<AVObject> avObjectList = new ArrayList<>();
        for (int i = 0; i < bloodPressureItemList.size(); i++) {
            BloodPressureItem item = bloodPressureItemList.get(i);
            Log.d(TAG, "upLoadBloodPressure: " + item.getUser().getObject_id());
            String object_id = item.getObject_id();
            String user_object_id = item.getUser().getObject_id();
            Date date = item.getDate();
            Date lastModifyDate = item.getLastModifyDate();
            float systolicPressure = item.getSystolicPressure();
            float diastolicPressure = item.getDiastolicPressure();
            AVObject avObject;
            if (object_id != null && object_id.trim().length() != 0) {
                Log.d(TAG, "upLoadBloodPressure: " + "云端存在此记录，更新");
                avObject = AVObject.createWithoutData("bloodpressureitem", object_id);
            } else {
                Log.d(TAG, "upLoadBloodPressure: " + "云端不存在，新建");
                avObject = new AVObject("bloodpressureitem");
            }
            avObject.put("date", date);
            avObject.put("systolicpressure", systolicPressure);
            avObject.put("diastolicpressure", diastolicPressure);
            avObject.put("user_object_id", user_object_id);
            avObject.put("lastmodifydate", lastModifyDate);
            avObjectList.add(avObject);
        }

        AVObject.saveAllInBackground(avObjectList, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    for (int i = 0; i < avObjectList.size(); i++) {
                        BloodPressureItem item = bloodPressureItemList.get(i);
                        AVObject avObject = avObjectList.get(i);
                        if (item.getObject_id() != null && item.getObject_id().trim().length() != 0) {
                            item.setCloudStorage(true);
                        } else {
                            item.setObject_id(avObject.getObjectId());
                            item.setCloudStorage(true);
                        }
                        String time = String.valueOf(item.getDate().getTime());
                        item.updateAll("date=?", time);
                    }
                } else {
                    Log.d(TAG, "done: " + e.getMessage());
                }

            }
        });

    }

    private static void upLoadBloodSugar() {
        final List<BloodSugarItem> bloodSugarItemList = DataSupport.where("iscloudstorage=?", "0").find(BloodSugarItem.class, true);
        Log.d(TAG, "upLoadBloodSugar: " + bloodSugarItemList.size());
        final List<AVObject> avObjectList = new ArrayList<>();
        for (int i = 0; i < bloodSugarItemList.size(); i++) {
            BloodSugarItem item = bloodSugarItemList.get(i);
            Log.d(TAG, "upLoadBloodSugar: " + item.getUser().getObject_id());
            String object_id = item.getObject_id();
            String user_object_id = item.getUser().getObject_id();
            Date date = item.getDate();
            Date lastModifyDate = item.getLastModifyDate();
            float beforeBreakfast = item.getBeforeBreakfast();
            float afterBreakfast = item.getAfterBreakfast();
            float beforeLunch = item.getBeforeLunch();
            float afterLunch = item.getAfterLunch();
            float beforeSupper = item.getBeforeSupper();
            float afterSupper = item.getAfterSupper();
            float beforeSleep = item.getBeforeSleep();
            float beforeDawn = item.getBeforeDawn();
            AVObject avObject;
            if (object_id != null && object_id.trim().length() != 0) {
                Log.d(TAG, "upLoadBloodSugar: " + "云端存在此记录，更新");
                avObject = AVObject.createWithoutData("bloodsugaritem", object_id);
            } else {
                Log.d(TAG, "upLoadBloodSugar: " + "云端不存在，新建");
                avObject = new AVObject("bloodsugaritem");
            }
            avObject.put("date", date);
            avObject.put("lastmodifydate", lastModifyDate);
            avObject.put("beforebreakfast", beforeBreakfast);
            avObject.put("afterbreakfast", afterBreakfast);
            avObject.put("beforelunch", beforeLunch);
            avObject.put("afterlunch", afterLunch);
            avObject.put("beforesupper", beforeSupper);
            avObject.put("aftersupper", afterSupper);
            avObject.put("beforesleep", beforeSleep);
            avObject.put("beforedawn", beforeDawn);
            avObject.put("user_object_id", user_object_id);
            avObjectList.add(avObject);
        }

        AVObject.saveAllInBackground(avObjectList, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    for (int i = 0; i < avObjectList.size(); i++) {
                        BloodSugarItem item = bloodSugarItemList.get(i);
                        AVObject avObject = avObjectList.get(i);
                        if (item.getObject_id() != null && item.getObject_id().trim().length() != 0) {
                            item.setCloudStorage(true);
                        } else {
                            item.setObject_id(avObject.getObjectId());
                            item.setCloudStorage(true);
                        }
                        String time = String.valueOf(item.getDate().getTime());
                        item.updateAll("date=?", time);
                    }
                } else {
                    Log.d(TAG, "done: " + e.getMessage());
                }

            }
        });

    }

    private static void upLoadHeartRate() {
        final List<HeartRateItem> heartRateItemList = DataSupport.where("iscloudstorage=?", "0").find(HeartRateItem.class, true);
       final List<AVObject> avObjectList = new ArrayList<>();
        Log.d(TAG, "upLoadHeartRate: " + heartRateItemList.size());
        for (int i = 0; i < heartRateItemList.size(); i++) {
            HeartRateItem item = heartRateItemList.get(i);
            Log.d(TAG, "upLoadHeartRate: " + item.getUser().getObject_id());
            String object_id = item.getObject_id();
            String user_object_id = item.getUser().getObject_id();
            Date date = item.getDate();
            int rate=item.getRate();
            AVObject avObject;
            if (object_id != null && object_id.trim().length() != 0) {
                Log.d(TAG, "upLoadHeartRate: " + "云端存在此记录，更新");
                avObject = AVObject.createWithoutData("heartrateitem", object_id);
            } else {
                Log.d(TAG, "upHeartRatePressure: " + "云端不存在，新建");
                avObject = new AVObject("heartrateitem");
            }
            avObject.put("date", date);
            avObject.put("rate", rate);
            avObject.put("user_object_id", user_object_id);
            avObjectList.add(avObject);
        }

        AVObject.saveAllInBackground(avObjectList, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    for (int i = 0; i < avObjectList.size(); i++) {
                        HeartRateItem item = heartRateItemList.get(i);
                        AVObject avObject = avObjectList.get(i);
                        if (item.getObject_id() != null && item.getObject_id().trim().length() != 0) {
                            item.setCloudStorage(true);
                        } else {
                            item.setObject_id(avObject.getObjectId());
                            item.setCloudStorage(true);
                        }
                        String time = String.valueOf(item.getDate().getTime());
                        item.updateAll("date=?", time);
                    }
                } else {
                    Log.d(TAG, "done: " + e.getMessage());
                }

            }
        });

    }


    private static void deleteItemsFromCloud() {
        List<DeletedItems> deletedItemsList = DataSupport.findAll(DeletedItems.class);
        Log.d(TAG, "deleteItemsFromCloud: " + deletedItemsList.size());
        for (final DeletedItems item :
                deletedItemsList) {
            AVObject itemToDelete = AVObject.createWithoutData(item.getTableNameOfItem(), item.getObjectIdOfItem());
            itemToDelete.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        item.delete();
                    }
                }
            });
        }
    }


}
