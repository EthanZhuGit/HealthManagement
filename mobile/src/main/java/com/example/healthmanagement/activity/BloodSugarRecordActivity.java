package com.example.healthmanagement.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.R;
import com.example.healthmanagement.customview.WheelView;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.User;
import com.lost.zou.scaleruler.utils.DrawUtil;
import com.lost.zou.scaleruler.view.DecimalScaleRulerView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BloodSugarRecordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG" + "BloodSugarRecordA";

    public static final int RESULT_CODE_CHANGE_BSRA = 1;
    public static final int RESULT_CODE_NO_CHANGE_BSRA = 0;


    private Button setDateBtn;
    private Button setTimeBtn;
    private DecimalScaleRulerView highPressureChoose;

    private TextView highPressureNum;

    private Date dateDefault;
    private Calendar calendar;


    private int selectedPositionInWheelView = 3;
    private float numChoosed = 6.0f;

    private Date lastModifyDate;

    private MyApplication myApplication;
    private String user_id;

    private String[] wheelViewItems = new String[]{"凌晨", "早餐前", "早餐后", "午餐前", "午餐后", "晚餐前", "晚餐后", "睡前"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_record);
        myApplication = (MyApplication) getApplicationContext();
        user_id = myApplication.getUid();
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


        } else {
            lastModifyDate = HelpUtils.getDateWithoutMillionSec(new Date());
            dateDefault = LocalDateBaseHelper.getStartTimeOfSpecifiedDaysAgo(new Date(), 0);
            dateDefault = HelpUtils.getDateWithoutMillionSec(dateDefault);
            calendar = Calendar.getInstance();
            calendar.setTime(dateDefault);
            Log.d(TAG, "onCreate: dateDefault" + dateDefault);

        }
        showTimeInButton(dateDefault);
        setDateBtn.setOnClickListener(this);
        setTimeBtn.setOnClickListener(this);
        highPressureChoose.setValueChangeListener(new DecimalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float v) {
                highPressureNum.setText(String.valueOf(v));
                String s = new DecimalFormat("###.#").format(v);
                numChoosed = Float.valueOf(s);
            }
        });


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_confirm);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BloodSugarItem> items = DataSupport.
                        where("date=? and user_id=?", String.valueOf(dateDefault.getTime()), user_id)
                        .find(BloodSugarItem.class);
                int size = items.size();

                List<User> users = DataSupport.where("id=?", user_id).find(User.class);
                User user = users.get(0);
                if (size == 0) {
                    BloodSugarItem item = new BloodSugarItem();
                    switch (selectedPositionInWheelView) {
                        case 0:
                            item.setDate(dateDefault);
                            item.setBeforeDawn(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);
                            break;
                        case 1:
                            item.setDate(dateDefault);
                            item.setBeforeBreakfast(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 2:
                            item.setDate(dateDefault);
                            item.setAfterBreakfast(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 3:
                            item.setDate(dateDefault);
                            item.setBeforeLunch(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 4:
                            item.setDate(dateDefault);
                            item.setAfterLunch(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 5:
                            item.setDate(dateDefault);
                            item.setBeforeSupper(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 6:
                            item.setDate(dateDefault);
                            item.setAfterSupper(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);

                            break;
                        case 7:
                            item.setDate(dateDefault);
                            item.setBeforeSleep(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            Log.d(TAG, "onClick: " + dateDefault+" "+selectedPositionInWheelView+" "+numChoosed+" "+lastModifyDate);
                            break;
                    }
//                    user.getBloodSugarItemList().add(item);
//                    user.save();
                    item.setUser(user);
                    if (item.save()) {
                        Log.d(TAG, "onClick: " + "血糖保存成功");
                    } else {
                        Log.d(TAG, "onClick: " + "血糖保存失败");
                    }

//                    user.saveOrUpdate("id=?", user_id);

                } else if (size == 1) {
                    BloodSugarItem item = items.get(0);
                    switch (selectedPositionInWheelView) {
                        case 0:
                            item.setBeforeDawn(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 1:
                            item.setBeforeBreakfast(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 2:
                            item.setAfterBreakfast(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 3:
                            item.setBeforeLunch(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 4:
                            item.setAfterLunch(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 5:
                            item.setBeforeSupper(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 6:
                            item.setAfterSupper(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                        case 7:
                            item.setBeforeSleep(numChoosed);
                            item.setLastModifyDate(lastModifyDate);
                            break;
                    }
                    String time=String.valueOf(item.getDate().getTime());

//                    items1.add(item);
//                    user.getBloodSugarItemList().add(item);
//                    user.saveOrUpdate("id=?", user_id);
//                    user.save();
                    int i = item.updateAll("date=?", time);
                    Log.d(TAG, "onClick: 血糖 " + i + "条记录修改");
                } else {
                    Toast.makeText(BloodSugarRecordActivity.this,
                            "记录错误", Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_CODE_CHANGE_BSRA);
                finish();
            }
        });
    }


    private void initView() {
        setDateBtn = (Button) findViewById(R.id.btn_set_date);
        setTimeBtn = (Button) findViewById(R.id.btn_set_time);
        highPressureChoose = (DecimalScaleRulerView) findViewById(R.id.high);

        highPressureNum = (TextView) findViewById(R.id.txt_high_num);
        highPressureNum.setText(String.valueOf(6.0));
        highPressureChoose.setParam(DrawUtil.dip2px(30), DrawUtil.dip2px(200), DrawUtil.dip2px(150),
                DrawUtil.dip2px(100), DrawUtil.dip2px(9), DrawUtil.dip2px(40));
        highPressureChoose.initViewParam(numChoosed, 0.0f, 15.0f, 1);


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
                setResult(RESULT_CODE_NO_CHANGE_BSRA);
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
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(wheelViewItems));
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                selectedPositionInWheelView = selectedIndex-2;
                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item+" "+selectedPositionInWheelView);

            }
        });

        new android.app.AlertDialog.Builder(this)
                .setTitle("选择时间段")
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTimeBtn.setText("时间  "+wheelViewItems[selectedPositionInWheelView]);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CODE_NO_CHANGE_BSRA);
                        finish();
                    }
                })
                .show();
    }

    private void showTimeInButton(Date date) {
        setDateBtn.setText("日期" + "   " + new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date));
        setTimeBtn.setText("时间" + "   " + wheelViewItems[selectedPositionInWheelView]);
//        setTimeBtn.setText("时间" + "   " + new SimpleDateFormat("ahh:mm",Locale.CHINA).format(date));
    }
}
