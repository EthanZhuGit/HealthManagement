package com.example.healthmanagement.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/10 0010.
 */

public class Record {

    public static final String BLOOD_PRESSURE = "blood_pressure";
    public static final String BLOOD_SUGAR = "blood_sugar";
    public static final String STEP_COUNT = "step_count";
    public static final String HEART_RATE = "heart_rate";
    public static final String BLOOD_OXYGEN = "blood_oxygen";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Record(String name, HashMap<Date, BloodPressureItem> bloodPressureDataMap, HashMap<Date, BloodSugarItem> bloodSugarDataMap) {
        this.name = name;
        this.bloodPressureDataMap = bloodPressureDataMap;
        this.bloodSugarDataMap = bloodSugarDataMap;
    }

    public Record(String name, List<BloodPressureItem> bloodPressureItemList, List<BloodSugarItem> bloodSugarItemList) {
        this.name = name;
        this.bloodPressureItemList = bloodPressureItemList;
        this.bloodSugarItemList = bloodSugarItemList;
    }

    /*****************************   blood pressure   **************************************/
    private HashMap<Date, BloodPressureItem> bloodPressureDataMap;

    public void setBloodPressureDataMap(HashMap<Date, BloodPressureItem> bloodPressureDataMap) {
        this.bloodPressureDataMap = bloodPressureDataMap;
    }

    public HashMap<Date, BloodPressureItem> getBloodPressureDataMap() {
        return bloodPressureDataMap;
    }

    /************************    blood sugar  *******************************************/

    private HashMap<Date, BloodSugarItem> bloodSugarDataMap;


    public HashMap<Date, BloodSugarItem> getBloodSugarDataMap() {
        return bloodSugarDataMap;
    }

    private List<BloodPressureItem> bloodPressureItemList;
    public List<BloodPressureItem> getBloodPressureItemList() {
        return bloodPressureItemList;
    }
    public void setBloodPressureItemList(List<BloodPressureItem> bloodPressureItemList) {
        this.bloodPressureItemList = bloodPressureItemList;
    }

    private List<BloodSugarItem> bloodSugarItemList;

    public List<BloodSugarItem> getBloodSugarItemList() {
        return bloodSugarItemList;
    }

    public void setBloodSugarItemList(List<BloodSugarItem> bloodSugarItemList) {
        this.bloodSugarItemList = bloodSugarItemList;
    }
}
