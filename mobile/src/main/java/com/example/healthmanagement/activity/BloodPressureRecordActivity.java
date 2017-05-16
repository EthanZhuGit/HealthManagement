package com.example.healthmanagement.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.healthmanagement.R;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.fragment.HomeFragment;
import com.example.healthmanagement.model.BloodPressureItem;
import com.lost.zou.scaleruler.utils.DrawUtil;
import com.lost.zou.scaleruler.view.DecimalScaleRulerView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BloodPressureRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG" + "BloodPressureRActi";
    private float HP = 120.0f;
    private float maxHP = 250.0f;
    private float minHP = 0.0f;
    private float LP = 80.0f;
    private float maxLP = 200.0f;
    private float minLP = 0.0f;


    private Button setDateBtn;
    private Button setTimeBtn;
    private Button confirmBtn;
    private Button cancelBtn;
    private DecimalScaleRulerView highPressureChoose;
    private DecimalScaleRulerView lowPressureChoose;
    private TextView highPressureNum;
    private TextView lowPressureNum;

    private Date dateDefault;
    private Calendar calendar;

    private BloodPressureItem bloodPressureItem;

    private float highPressure;
    private float lowPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_record);

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


        } else {
            dateDefault = new Date();
            calendar = Calendar.getInstance();
            calendar.setTime(dateDefault);
            highPressure=HP;
            lowPressure=LP;
        }
        showTimeInButton(dateDefault);
        setDateBtn.setOnClickListener(this);
        setTimeBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        highPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                highPressureNum.setText(String.valueOf(v));
                String s = new DecimalFormat("###.#").format(v);
                highPressure = Float.valueOf(s);
            }
        });
        lowPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                lowPressureNum.setText(String.valueOf(v));
                String s = new DecimalFormat("###.#").format(v);
                lowPressure = Float.valueOf(s);
            }
        });
    }


    private void initView() {
        setDateBtn = (Button) findViewById(R.id.btn_set_date);
        setTimeBtn = (Button) findViewById(R.id.btn_set_time);
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
                showDatePicker();
                break;
            case R.id.btn_set_time:
                showTimePicker();
                break;
            case R.id.btn_confirm:
                bloodPressureItem = new BloodPressureItem();
                bloodPressureItem.setDate(dateDefault);
                bloodPressureItem.setSystolicPressure(highPressure);
                bloodPressureItem.setDiastolicPressure(lowPressure);
                bloodPressureItem.setLastModifyDate(dateDefault);
                Bundle bundle = new Bundle();
                bundle.putParcelable("bp_return", bloodPressureItem);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(HomeFragment.RESULT_CODE_RECORD_BLOODPRESSURE, intent);
                finish();
                break;
            case R.id.btn_cancel:
                if (bloodPressureItem != null) {

                }else {

                    finish();
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请按“确认”保存数据");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                dateDefault = calendar.getTime();
                showTimeInButton(dateDefault);

            }
        };
        new DatePickerDialog(this,R.style.Theme_AppCompat_DayNight_Dialog,dateSetListener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dateDefault=calendar.getTime();
                showTimeInButton(dateDefault);

            }
        };
        new TimePickerDialog(this,R.style.Theme_AppCompat_DayNight_Dialog, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void showTimeInButton(Date date) {
        setDateBtn.setText("日期" + "   " + new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date));
        setTimeBtn.setText("时间" + "   " + new SimpleDateFormat("ahh:mm",Locale.CHINA).format(date));
    }
}
