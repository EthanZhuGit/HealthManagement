package com.example.healthmanagement.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.R;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.HeartRateItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.lost.zou.scaleruler.utils.DrawUtil;
import com.lost.zou.scaleruler.view.DecimalScaleRulerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HeartRateRecordActivity extends AppCompatActivity
        implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "TAG" + "HeartRateRecord";

    public static final int RESULT_CODE_CHANGE_HRRA = 1;
    public static final int RESULT_CODE_NO_CHANGE_HRRA = 0;


    private Button setDateBtn;
    private Button setTimeBtn;

    private DecimalScaleRulerView highPressureChoose;
    private TextView heartRateNum;
    private Button deviceStatus;

    private boolean isDeviceAvailable = false;

    private Node node;
    private Date dateDefault;
    private Calendar calendar;

    private HeartRateItem heartRateItem;

    private float numChoosed = 100;
    private GoogleApiClient googleApiClient;

    private MyApplication myApplication;

    private MsgReceiver msgReceiver;
    private LocalBroadcastManager localBroadcastManager;

   private ProgressDialog progressDialog;

    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_record);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("heart_rate_from_device");
        msgReceiver = new MsgReceiver();
        localBroadcastManager.registerReceiver(msgReceiver, intentFilter);
        myApplication = (MyApplication) getApplicationContext();
        user_id=myApplication.getUid();
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


        } else {
            dateDefault = HelpUtils.getDateWithoutMillionSec(new Date());
            calendar = Calendar.getInstance();
            calendar.setTime(dateDefault);
        }
        showTimeInButton(dateDefault);

        highPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                heartRateNum.setText(String.valueOf(v));
                String s = new DecimalFormat("###.#").format(v);
                numChoosed = Float.valueOf(s);
            }
        });


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_confirm);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateDefault.after(new Date())) {
                    Toast.makeText(HeartRateRecordActivity.this, "时间不应该在当前时间之后", Toast.LENGTH_SHORT).show();
                    return;
                }
                HeartRateItem heartRateItem = new HeartRateItem();
                heartRateItem.setDate(dateDefault);
                heartRateItem.setRate((int)numChoosed);
                LocalDateBaseHelper.saveHeartRateItem(user_id, heartRateItem);
                setResult(RESULT_CODE_CHANGE_HRRA);
                finish();
            }
        });
        attemptToConnect();
    }

    private void initView() {
        setDateBtn = (Button) findViewById(R.id.btn_set_date);
        setTimeBtn = (Button) findViewById(R.id.btn_set_time);
        deviceStatus = (Button) findViewById(R.id.device_status);
        setDateBtn.setOnClickListener(this);
        setTimeBtn.setOnClickListener(this);
        deviceStatus.setOnClickListener(this);

        heartRateNum = (TextView) findViewById(R.id.txt_heart_rate);
        heartRateNum.setText(String.valueOf(100));

        highPressureChoose = (DecimalScaleRulerView) findViewById(R.id.high);
        highPressureChoose.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(200), DrawUtil.dip2px(150),
                DrawUtil.dip2px(100), DrawUtil.dip2px(9), DrawUtil.dip2px(40));
        highPressureChoose.initViewParam(numChoosed, 60, 200, 1);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_date:
                showDatePicker();
                break;
            case R.id.btn_set_time:
                showTimePicker();
                break;
            case R.id.device_status:
                if (isDeviceAvailable) {
                    Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "getHeartRate", null);
                    showProgressDialog();
                } else {
                    attemptToConnect();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("放弃数据？");
        builder.setMessage("界面将关闭且数据不会被存储");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CODE_NO_CHANGE_HRRA);
                finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                dateDefault = calendar.getTime();
                showTimeInButton(dateDefault);

            }
        };
        new DatePickerDialog(this, R.style.Theme_AppCompat_DayNight_Dialog, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dateDefault = calendar.getTime();
                showTimeInButton(dateDefault);

            }
        };
        new TimePickerDialog(this, R.style.Theme_AppCompat_DayNight_Dialog, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void attemptToConnect() {
        googleApiClient = new GoogleApiClient.Builder(HeartRateRecordActivity.this).addApi(Wearable.API).build();
        googleApiClient.connect();
        PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                final List<Node> nodes = result.getNodes();
                if (nodes != null && nodes.size() != 0) {
                    node = nodes.get(0);
                    isDeviceAvailable = true;
                    deviceStatus.setText("连接成功: ");
                    showMeasureConfirmDialog();
                } else {
                    deviceStatus.setText("无设备，点击尝试连接");
                }
            }
        });
    }

    private void showMeasureConfirmDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设备已连接");
        builder.setMessage("是否测量心率");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "getHeartRate", null);
                showProgressDialog();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("正在测量");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showTimeInButton(Date date) {
        setDateBtn.setText("日期" + "   " + new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date));
        setTimeBtn.setText("时间" + "   " + new SimpleDateFormat("ahh:mm", Locale.CHINA).format(date));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(msgReceiver);
    }

    class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int heartRate = intent.getIntExtra("heart_rate", 0);
            progressDialog.dismiss();
            heartRateNum.setText(heartRate+"");
            numChoosed=heartRate;
            highPressureChoose.initViewParam(numChoosed, 60, 200, 1);

        }
    }
}


