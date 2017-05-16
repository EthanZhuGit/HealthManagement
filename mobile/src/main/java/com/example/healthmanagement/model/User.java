package com.example.healthmanagement.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/12 0012.
 */

public class User extends DataSupport {
    public User() {
    }

    String username;
    @Column(unique = true)
    String phoneNum;
    int age;
    String sex;
    private List<BloodPressureItem> bloodPressureItemList = new ArrayList<>();
    private List<BloodSugarItem> bloodSugarItemList = new ArrayList<>();
    private List<HeartRateItem> heartRateItemList = new ArrayList<>();
    private List<BloodOxygenItem> bloodOxygenItemList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<BloodPressureItem> getBloodPressureItemList() {
        return bloodPressureItemList;
    }

    public void setBloodPressureItemList(List<BloodPressureItem> bloodPressureItemList) {
        this.bloodPressureItemList = bloodPressureItemList;
    }

    public List<BloodSugarItem> getBloodSugarItemList() {
        return bloodSugarItemList;
    }

    public void setBloodSugarItemList(List<BloodSugarItem> bloodSugarItemList) {
        this.bloodSugarItemList = bloodSugarItemList;
    }

    public List<HeartRateItem> getHeartRateItemList() {
        return heartRateItemList;
    }

    public void setHeartRateItemList(List<HeartRateItem> heartRateItemList) {
        this.heartRateItemList = heartRateItemList;
    }

    public List<BloodOxygenItem> getBloodOxygenItemList() {
        return bloodOxygenItemList;
    }

    public void setBloodOxygenItemList(List<BloodOxygenItem> bloodOxygenItemList) {
        this.bloodOxygenItemList = bloodOxygenItemList;
    }
}
