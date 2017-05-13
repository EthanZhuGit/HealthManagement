package com.example.healthmanagement;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String BLOOD = "blood_pressure";
    private ViewPager viewPager;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> fragmentList=new ArrayList<>();
    private String objectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    objectId = testObject.getObjectId();
//                    Log.d("saved","success!"+objectId);
//
//                }
//            }
//        });
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        Fragment homeFrag = HomeFragment.newInstance(objectId);
        Fragment healthNewsFrag = HealthNewsFragment.newInstance(1);
        Fragment myInfoFrag=MyInfoFragment.newInstance("1","2");
        fragmentList.add(homeFrag);
        fragmentList.add(healthNewsFrag);
        fragmentList.add(myInfoFrag);
        FragmentManager fragmentManager=getSupportFragmentManager();
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        bottomNavigationViewEx.setupWithViewPager(viewPager);
        bottomNavigationViewEx.enableItemShiftingMode(true);


    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{
        private FragmentManager fragmentManager;
        private List<Fragment> fragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentManager=fm;
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
}
