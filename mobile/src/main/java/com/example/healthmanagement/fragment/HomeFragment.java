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
import com.example.healthmanagement.customview.ListViewForScrollView;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.R;
import com.example.healthmanagement.activity.BloodPressureDetailActivity;
import com.example.healthmanagement.activity.BloodPressureRecordActivity;
import com.example.healthmanagement.activity.BloodSugarDetailActivity;
import com.example.healthmanagement.activity.BloodSugarRecordActivity;
import com.example.healthmanagement.activity.CardShowControlActivity;
import com.example.healthmanagement.adapter.CardListAdapter;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CardListAdapter.OnCardClickListener {

    private static final String TAG = "TAG" + "HomeFragment";
    private static final String USER_CLOUD_ID = "user_cloud_id";
    public static final int REQUEST_CODE_CARDCONTROL = 100;
    public static final int RESULT_CODE_CARDCONTROL = 101;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BPDA = 102;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BPRA = 103;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BSDA = 104;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BSRA = 105;

    private ListViewForScrollView cardListView;
    private Button btnCardShowControl;
    private CardListAdapter cardListAdapter;
    private String user_id;
    private Record record;
    private List<Record> records = new ArrayList<>();

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
            user_id = getArguments().getString(USER_CLOUD_ID);
            Log.d(TAG, "onCreate: " + user_id);
            MyApplication myApplication = (MyApplication) getContext().getApplicationContext();
            myApplication.setUid(user_id);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (requestCode) {
            case REQUEST_CODE_CARDCONTROL:
                bundle = data.getExtras();
                ArrayList<IsCardShow> l = bundle.getParcelableArrayList("list_return");
                cardShowControlArrayList.clear();
                cardShowControlArrayList.addAll(l);
                setRecordList();
                Log.d(TAG, "onActivityResult: " + records.size());
                cardListAdapter.notifyDataSetChanged();
                break;
            case REQUEST_CODE_RECORD_HOME_TO_BPDA:
                if (resultCode == BloodPressureDetailActivity.RESULT_CODE_CHANGE_BPDA) {
                    Log.d(TAG, "onActivityResult: " + "data change from bpda");
//                    bundle = data.getExtras();
//                    BloodPressureItem item = bundle.getParcelable("bp_return");
//                    LocalDateBaseHelper.saveBloodPressureItem("18086742831", item);
                    Record record = LocalDateBaseHelper.getBloodPressureRecord(user_id);
                    if (records.contains(record)) {
                        records.set(records.indexOf(record), record);
                    }
                    cardListAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onActivityResult: " + "date no change from bpda " + resultCode);
                }
                break;
            case REQUEST_CODE_RECORD_HOME_TO_BPRA:
                if (resultCode == BloodPressureRecordActivity.RESULT_CODE_CHANGE_BPRA) {
                    Log.d(TAG, "onActivityResult: " + "data change from bpra");
                    Record record = LocalDateBaseHelper.getBloodPressureRecord(user_id);
                    if (records.contains(record)) {
                        records.set(records.indexOf(record), record);
                    }
                    cardListAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_RECORD_HOME_TO_BSDA:
                if (resultCode == BloodSugarDetailActivity.RESULT_CODE_CHANGE_BSDA) {
                    Log.d(TAG, "onActivityResult: " + "data change form bsda");
                    Record record = LocalDateBaseHelper.getBloodSugarRecord(user_id);
                    if (records.contains(record)) {
                        records.set(records.indexOf(record), record);
                    }
                    cardListAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_RECORD_HOME_TO_BSRA:
                if (resultCode == BloodSugarRecordActivity.RESULT_CODE_CHANGE_BSRA) {
                    Log.d(TAG, "onActivityResult: " + "data change from bsra");
                    Record record = LocalDateBaseHelper.getBloodSugarRecord(user_id);
                    if (records.contains(record)) {
                        records.set(records.indexOf(record), record);
                    }
                    cardListAdapter.notifyDataSetChanged();
                }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
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

//        java.util.Date date = new java.util.Date();
//        LocalDateBaseHelper.saveBloodPressureItem("18086742831", new java.util.Date(1494217984000L), 100.0f, 90.0f, new java.util.Date(1494217984000L));
//        LocalDateBaseHelper.saveBloodPressureItem("18086742831",new java.util.Date(1494397812000L), 115.0f, 80.0f,new java.util.Date(1494397812000L));
//        LocalDateBaseHelper.saveBloodPressureItem("18086742831",new java.util.Date(1494292745000L), 130.0f, 100.0f,new java.util.Date(1494292745000L));
//        LocalDateBaseHelper.saveBloodPressureItem("18086742831",new java.util.Date(1494292749000L), 125.0f, 92.0f,new java.util.Date(1494292749000L));
//        LocalDateBaseHelper.saveBloodPressureItem("18086742831",date,150.0f,90.0f,date);
        setRecordList();

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
        cardListAdapter.setOnCardClickListener(this);
        cardListView.setAdapter(cardListAdapter);
        return view;
    }

    @Override
    public void onCardClick(View v, String name) {
        Log.d(TAG, "onCardClick: ");
        switch (v.getId()) {
            case R.id.layout_include_scatter:
                Intent intent;
                switch (name) {
                    case Record.BLOOD_PRESSURE:
                        intent = new Intent(getContext(), BloodPressureDetailActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_RECORD_HOME_TO_BPDA);
                        break;
                    case Record.BLOOD_SUGAR:
                        intent = new Intent(getContext(), BloodSugarDetailActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_RECORD_HOME_TO_BSDA);
                        break;
                }
                break;
            case R.id.btn_record:
                Intent intent1;
                switch (name) {
                    case Record.BLOOD_PRESSURE:
                        intent1 = new Intent(getContext(), BloodPressureRecordActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_RECORD_HOME_TO_BPRA);
                        break;
                    case Record.BLOOD_SUGAR:
                        intent1 = new Intent(getContext(), BloodSugarRecordActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_RECORD_HOME_TO_BSRA);
                        break;
                }
                break;
        }

    }

    private void setRecordList() {
        Log.d(TAG, "setRecordList: ");
        List<Record> r = new ArrayList<>();
        for (IsCardShow i :
                cardShowControlArrayList) {
            if (i.isShow()) {
                switch (i.getName()) {
                    case Record.BLOOD_OXYGEN:
                        r.add(LocalDateBaseHelper.getBloodOxygenRecord(user_id));
                        break;
                    case Record.BLOOD_PRESSURE:
                        r.add(LocalDateBaseHelper.getBloodPressureRecord(user_id));
                        break;
                    case Record.BLOOD_SUGAR:
                        r.add(LocalDateBaseHelper.getBloodSugarRecord(user_id));
                        break;
                    case Record.HEART_RATE:
                        r.add(LocalDateBaseHelper.getHeartRateRecord(user_id));
                        break;
                    default:
                        break;
                }
            }
        }
        records.clear();
        records.addAll(r);

    }

    @Override
    public void onStop() {
        super.onStop();
        HelpUtils.saveCardShowStatus(getContext(), cardShowControlArrayList);
        Log.d(TAG, "onStop: ");
    }
}




