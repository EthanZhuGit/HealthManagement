package com.example.healthmanagement.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.datebase.CloudDataBaseHelper;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.fragment.HealthNewsFragment;
import com.example.healthmanagement.fragment.HomeFragment;
import com.example.healthmanagement.fragment.MyInfoFragment;
import com.example.healthmanagement.R;
import com.example.healthmanagement.model.BloodOxygenItem;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.HeartRateItem;
import com.example.healthmanagement.model.User;
import com.example.healthmanagement.service.CloudStorageService;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG" + "MainActivity";

    private ViewPager viewPager;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        AVUser userCloud = AVUser.getCurrentUser();
        String object_id = userCloud.getObjectId();
        Log.d(TAG, "onCreate: " + object_id);
        String phonenun = userCloud.getMobilePhoneNumber();
        String name = userCloud.getString("name");
        int age = userCloud.getInt("age");
        String sex = userCloud.getString("sex");

        User userLocal;
        List<User> userLocalList = DataSupport.where("object_id=?", object_id).find(User.class);
        Log.d(TAG, "onCreate: " + userLocalList.size());
        if (userLocalList.size() == 0) {
            userLocal = new User();
            userLocal.setUsername(name);
            userLocal.setPhoneNum(phonenun);
            userLocal.setObject_id(object_id);
            userLocal.setAge(age);
            userLocal.setSex(sex);
            userLocal.save();
            user_id = String.valueOf(userLocal.getId());
        } else {
            userLocal = userLocalList.get(0);
            user_id = String.valueOf(userLocal.getId());
        }


        Log.d(TAG, "onCreate: user_id " + user_id);
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setUid(user_id);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        Fragment homeFrag = HomeFragment.newInstance();
        Fragment healthNewsFrag = HealthNewsFragment.newInstance(1);
        Fragment myInfoFrag = MyInfoFragment.newInstance("1", "2");
        fragmentList.add(homeFrag);
        fragmentList.add(healthNewsFrag);
        fragmentList.add(myInfoFrag);
        FragmentManager fragmentManager = getSupportFragmentManager();
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        bottomNavigationViewEx.setupWithViewPager(viewPager);
        bottomNavigationViewEx.enableItemShiftingMode(true);


    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;
        private List<Fragment> fragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentManager = fm;
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloudStorageService.startActionUpload(this);
        Log.d(TAG, "onDestroy: ");
    }


}
