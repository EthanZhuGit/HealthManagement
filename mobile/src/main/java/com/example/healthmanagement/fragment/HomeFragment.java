package com.example.healthmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.activity.HeartRateDetailActivity;
import com.example.healthmanagement.activity.HeartRateRecordActivity;
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
import com.example.healthmanagement.model.BloodOxygenItem;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.HeartRateItem;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;
import com.example.healthmanagement.model.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements CardListAdapter.OnCardClickListener {

    private static final String TAG = "TAG" + "HomeFragment";
    public static final int REQUEST_CODE_CARDCONTROL = 100;
    public static final int RESULT_CODE_CARDCONTROL = 101;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BPDA = 102;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BPRA = 103;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BSDA = 104;
    public static final int REQUEST_CODE_RECORD_HOME_TO_BSRA = 105;
    public static final int REQUEST_CODE_RECORD_HOME_TO_HRDA = 106;
    public static final int REQUEST_CODE_RECORD_HOME_TO_HRRA = 107;


    private ListViewForScrollView cardListView;
    private Button btnCardShowControl;
    private CardListAdapter cardListAdapter;
    private String user_id;
    private List<Record> records = new ArrayList<>();

    private ArrayList<IsCardShow> cardShowControlArrayList = new ArrayList<>();
    private boolean isBPDataDownLoad = false;
    private boolean isBSDataDownLoad = false;

    private User user;
    private String object_id;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication myApplication = (MyApplication) getContext().getApplicationContext();
        user_id = myApplication.getUid();
        List<User> users = DataSupport.where("id=?", user_id).find(User.class);
        user = users.get(0);
        object_id = user.getObject_id();
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
                HelpUtils.saveCardShowStatus(getContext(), cardShowControlArrayList);

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
                break;
            case REQUEST_CODE_RECORD_HOME_TO_HRDA:
                if (resultCode == HeartRateDetailActivity.RESULT_CODE_CHANGE_HRDA) {
                    Log.d(TAG, "onActivityResult: " + "data change from hrda");
                    Record record = LocalDateBaseHelper.getHeartRateRecord(user_id);
                    if (records.contains(record)) {
                        records.set(records.indexOf(record), record);
                    }
                    cardListAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_RECORD_HOME_TO_HRRA:
                if (resultCode == HeartRateRecordActivity.RESULT_CODE_CHANGE_HRRA) {
                    Log.d(TAG, "onActivityResult: " + "data change from hrra");
                    Record record = LocalDateBaseHelper.getHeartRateRecord(user_id);
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

        setRecordList();

        cardListAdapter = new CardListAdapter(getContext(), R.layout.item_card, records);
        cardListAdapter.setOnCardClickListener(this);
        cardListView.setAdapter(cardListAdapter);

        //从云端下载数据
        if (DataSupport.count(BloodPressureItem.class) == 0
                && DataSupport.count(BloodSugarItem.class) == 0
                && DataSupport.count(HeartRateItem.class) == 0
                && DataSupport.count(BloodOxygenItem.class) == 0) {
            downLoadBloodSugar(object_id, user);
            downLoadBloodPressure(object_id, user);
        }
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
                    case Record.HEART_RATE:
                        intent = new Intent(getContext(), HeartRateDetailActivity.class);
                        startActivityForResult(intent,REQUEST_CODE_RECORD_HOME_TO_HRDA);
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
                    case Record.HEART_RATE:
                        intent1 = new Intent(getContext(), HeartRateRecordActivity.class);
                        startActivityForResult(intent1,REQUEST_CODE_RECORD_HOME_TO_HRRA);
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
        Log.d(TAG, "onStop: ");
    }

    private void downLoadBloodSugar(final String objectIdOfUser, final User user) {
        AVQuery<AVObject> query = new AVQuery<>("bloodsugaritem");
        query.selectKeys(Arrays.
                asList("date", "beforebreakfast", "afterbreakfast",
                        "beforelunch", "afterlunch", "beforesupper",
                        "aftersupper", "beforesleep", "beforedawn", "lastmodifydate"));
        query.whereEqualTo("user_object_id", objectIdOfUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.d(TAG, "done: download bs" + list.size());
                    for (AVObject item : list) {
                        String object_id = item.getObjectId();
                        Date date = item.getDate("date");
                        Date lastmodifydate = item.getDate("lastmodifydate");
                        float beforeBreakfast = item.getNumber("beforebreakfast").floatValue();
                        float afterBreakfast = item.getNumber("afterbreakfast").floatValue();
                        float beforeLunch =  item.getNumber("beforelunch").floatValue();
                        float afterLunch = item.getNumber("afterlunch").floatValue();
                        float beforeSupper = item.getNumber("beforesupper").floatValue();
                        float afterSupper =item.getNumber("aftersupper").floatValue();
                        float beforeSleep = item.getNumber("beforesleep").floatValue();
                        float beforeDawn = item.getNumber("beforedawn").floatValue();
                        BloodSugarItem bloodSugarItem = new BloodSugarItem();
                        bloodSugarItem.setObject_id(object_id);
                        bloodSugarItem.setDate(date);
                        bloodSugarItem.setLastModifyDate(lastmodifydate);
                        bloodSugarItem.setBeforeBreakfast(beforeBreakfast);
                        bloodSugarItem.setAfterBreakfast(afterBreakfast);
                        bloodSugarItem.setBeforeLunch(beforeLunch);
                        bloodSugarItem.setAfterLunch(afterLunch);
                        bloodSugarItem.setBeforeSupper(beforeSupper);
                        bloodSugarItem.setAfterSupper(afterSupper);
                        bloodSugarItem.setBeforeSleep(beforeSleep);
                        bloodSugarItem.setBeforeDawn(beforeDawn);
                        bloodSugarItem.setCloudStorage(true);
                        bloodSugarItem.setUser(user);
                        bloodSugarItem.save();
                    }
                    isBSDataDownLoad = true;
                    if (isBPDataDownLoad && isBSDataDownLoad) {
                        setRecordList();
                        cardListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "done: " + "download complete");
                    }
                } else {
                    Log.d(TAG, "done: " + e.getMessage());
                }
            }
        });

    }

    private void downLoadBloodPressure(String objectIdOfUser, final User user) {
        AVQuery<AVObject> query = new AVQuery<>("bloodpressureitem");
        query.selectKeys(Arrays.asList("date", "systolicpressure", "diastolicpressure", "lastmodifydate"));
        query.whereEqualTo("user_object_id", objectIdOfUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.d(TAG, "done: download bp" + list.size());
                    for (AVObject object : list) {
                        String object_id = object.getObjectId();
                        Date date = object.getDate("date");
                        Date lastmodifydate = object.getDate("lastmodifydate");
                        float systolicpressure = object.getNumber("systolicpressure").floatValue();
                        float diastolicpressure =object.getNumber("diastolicpressure").floatValue();
                        BloodPressureItem item = new BloodPressureItem();
                        item.setObject_id(object_id);
                        item.setDate(date);
                        item.setLastModifyDate(lastmodifydate);
                        item.setSystolicPressure(systolicpressure);
                        item.setDiastolicPressure(diastolicpressure);
                        item.setCloudStorage(true);
                        item.setUser(user);
                        item.save();
                    }
                    isBPDataDownLoad = true;
                    if (isBPDataDownLoad && isBSDataDownLoad) {
                        setRecordList();
                        cardListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "done: " + "download complete");
                    }
                } else {
                    Log.d(TAG, "done: " + e.getMessage());
                }
            }
        });


    }
}




