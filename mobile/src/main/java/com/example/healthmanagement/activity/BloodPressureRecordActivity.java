package com.example.healthmanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthmanagement.R;
import com.lost.zou.scaleruler.utils.DrawUtil;
import com.lost.zou.scaleruler.view.DecimalScaleRulerView;

import java.util.Date;

public class BloodPressureRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG" + "BloodPressureRActi";
    private float HP = 120;
    private float maxHP = 250;
    private float minHP = 0;
    private float LP = 80;
    private float maxLP = 200;
    private float minLP = 0;


    private Button setDateBtn;
    private Button setTimeBtn;
    private Button confirmBtn;
    private Button cancelBtn;
    private DecimalScaleRulerView highPressureChoose;
    private DecimalScaleRulerView lowPressureChoose;
    private TextView highPressureNum;
    private TextView lowPressureNum;

    private Date date;
    private 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_record);
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

        }
        setDateBtn.setOnClickListener(this);
        setTimeBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        highPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                highPressureNum.setText(String.valueOf(v));
                Log.d(TAG, "onValueChange: " + v);
            }
        });
        lowPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                lowPressureNum.setText(String.valueOf(v));
                Log.d(TAG, "onValueChange: " + v);
            }
        });
    }


    private void initView() {
        setDateBtn = (Button) findViewById(R.id.btn_set_date);
        setTimeBtn = (Button) findViewById(R.id.btn_set_time);
        Date date = new Date();
        setDateBtn.setText("日期" + "   " + new java.sql.Date(date.getTime()));
        setTimeBtn.setText("时间" + "   " + new java.sql.Date(date.getTime()));
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        highPressureChoose = (DecimalScaleRulerView) findViewById(R.id.high);
        lowPressureChoose = (DecimalScaleRulerView) findViewById(R.id.low);
        highPressureNum = (TextView) findViewById(R.id.txt_high_num);
        lowPressureNum = (TextView) findViewById(R.id.txt_low_num);
        highPressureNum.setText(String.valueOf(HP));
        lowPressureNum.setText(String.valueOf(LP));
        highPressureChoose.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(200), DrawUtil.dip2px(150),
                DrawUtil.dip2px(100), DrawUtil.dip2px(9), DrawUtil.dip2px(40));
        highPressureChoose.initViewParam(HP, minHP, maxHP, 1);
        lowPressureChoose.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(200), DrawUtil.dip2px(150),
                DrawUtil.dip2px(100), DrawUtil.dip2px(9), DrawUtil.dip2px(40));
        lowPressureChoose.initViewParam(LP, minLP, maxLP, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_date:

        }

    }


    private Date showDatePicker(boolean isShowTimePickerNext) {

    }
}
