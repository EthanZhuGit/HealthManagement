package com.example.healthmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.ListViewForScrollView;
import com.example.healthmanagement.R;
import com.example.healthmanagement.activity.CardShowControlActivity;
import com.example.healthmanagement.adapter.CardListAdapter;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.BloodOxygenRecord;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CardListAdapter.OnRecordButtonClickListener {

    private static final String TAG = "TAG" + "HomeFragment";
    private static final String USER_CLOUD_ID = "user_cloud_id";
    public static final int REQUEST_CODE_CARDCONTROL = 100;
    public static final int RESULT_CODE_CARDCONTROL = 101;

    private ListViewForScrollView cardListView;
    private Button btnCardShowControl;
    private CardListAdapter cardListAdapter;
    private String userCloudId;
    private Record record;
    private List<Record> records = new ArrayList<>();
    private List<BloodPressureItem> bloodPressureItemListTest = new ArrayList<>();

    private ArrayList<IsCardShow> cardShowControlArrayList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String s) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_CLOUD_ID, s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userCloudId = getArguments().getString(USER_CLOUD_ID);
            Log.d(TAG, "onCreate: " + userCloudId);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CARDCONTROL:
                Bundle bundle = data.getExtras();
                ArrayList<IsCardShow> l = bundle.getParcelableArrayList("list_return");
                cardShowControlArrayList.clear();
                cardShowControlArrayList.addAll(l);
                setRecordList();
                Log.d(TAG, "onActivityResult: " + records.size());
                cardListAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cardListView = (ListViewForScrollView) view.findViewById(R.id.card_list);

        cardShowControlArrayList = HelpUtils.getCardShowControlList(getContext());

        btnCardShowControl = (Button) view.findViewById(R.id.btn_card_show_control);
        btnCardShowControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CardShowControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list_send", cardShowControlArrayList);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_CARDCONTROL);

            }
        });

        setRecordList();


//        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494397812000L), 115.0f, 80.0f));
//        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494292745000L), 130.0f, 100.0f));
//        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494292749000L), 125.0f, 92.0f));
//        DataSupport.saveAll(bloodPressureItemListTest);

//        User user = new User();
//        user.setUsername("朱一新");
//        user.setPhoneNum("18086742831");
//        user.setAge(22);
//        user.setSex("男");
//        user.setBloodPressureItemList(bloodPressureItemListTest);
//        user.save();
//
//        BloodPressureItem item = new BloodPressureItem(new java.util.Date(), 150.0f, 90.0f);
//        item.save();
//        user.getBloodPressureItemList().add(item);
//        user.saveOrUpdate("phonenum=?", "18086742831");
//        String startTime = String.valueOf(LocalDateBaseHelper.getSenvenDaysBeforStartTime().getTime());
//        String startTime = "1494683638763";
//        String endTime = String.valueOf(new java.util.Date().getTime());
//        List<BloodPressureItem> q = DataSupport.where("date>? and date<? and user_id=?",startTime,endTime,"1").find(BloodPressureItem.class);
//        for (BloodPressureItem b :
//                q) {
//            Log.d(TAG, "onCreateView: " + b.getDate().getTime());
//        }


//        List<BloodPressureItem> q = DataSupport.where("user_id=?", "1").find(BloodPressureItem.class);
//        record = new BloodPressureRecord(bloodPressureItemListTest);
//        records.add(record);
        cardListAdapter = new CardListAdapter(getContext(), R.layout.card_item, records);
        cardListView.setAdapter(cardListAdapter);
        return view;
    }

    @Override
    public void onRecordButtonClick(String name) {
        switch (name) {
            case Record.BLOOD_PRESSURE:

        }
    }

    private void setRecordList() {
        List<Record> r=new ArrayList<>();
        for (IsCardShow i :
                cardShowControlArrayList) {
            if (i.isShow()) {
                switch (i.getName()) {
                    case Record.BLOOD_OXYGEN:
                        r.add(LocalDateBaseHelper.getOneWeekBloodOxygenRecord());
                        break;
                    case Record.BLOOD_PRESSURE:
                        r.add(LocalDateBaseHelper.getOnWeekBloodPressureRecord());
                        break;
                    case Record.BLOOD_SUGAR:
                        r.add(LocalDateBaseHelper.getOneWeekBloodSugarRecord());
                        break;
                    case Record.HEART_RATE:
                        r.add(LocalDateBaseHelper.getOneWeekHeartRateRecord());
                        break;
                }
            }
        }
        records.clear();
        records.addAll(r);
    }
}




