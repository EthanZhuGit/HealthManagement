package com.example.healthmanagement.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.healthmanagement.fragment.HealthNewsFragment;
import com.example.healthmanagement.fragment.HomeFragment;
import com.example.healthmanagement.fragment.MyInfoFragment;
import com.example.healthmanagement.R;
import com.example.healthmanagement.model.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG" + "MainActivity";

    private ViewPager viewPager;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> fragmentList=new ArrayList<>();
    private String user_id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
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

        if (!DataSupport.isExist(User.class, "id=?", "1")) {
            User user = new User();
            user.setUsername("朱一新");
            user.setPhoneNum("18086742831");
            user.setAge(22);
            user.setSex("男");
            user.setObject_id("18086742831");
            user.save();
            Log.d(TAG, "onCreate: " + " user save");
        }





        viewPager= (ViewPager) findViewById(R.id.view_pager);
        bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bnve);
        Fragment homeFrag = HomeFragment.newInstance(user_id);
        Fragment healthNewsFrag = HealthNewsFragment.newInstance(1);
        Fragment myInfoFrag= MyInfoFragment.newInstance("1","2");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
