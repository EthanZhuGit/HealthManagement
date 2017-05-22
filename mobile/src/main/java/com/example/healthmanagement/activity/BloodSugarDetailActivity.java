package com.example.healthmanagement.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.R;
import com.example.healthmanagement.customview.DetailTextView;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.BloodSugarItemForChart;
import com.example.healthmanagement.model.BloodSugarRecord;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;


import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class BloodSugarDetailActivity extends AppCompatActivity {
    private static final String TAG = "TAG" + "BloodSugarDetailActivit";
    public static final int REQUEST_CODE_FROM_BSDA = 1000;
    public static final int RESULT_CODE_CHANGE_BSDA = 1001;
    public static final int RESULT_CODE_NO_CHANGE_BSDA = 1003;

    private TextView txtDate;
    private DetailTextView txtBeforeDawn;
    private DetailTextView txtBeforeBreakfast;
    private DetailTextView txtAfterBreakfast;
    private DetailTextView txtBeforeLunch;
    private DetailTextView txtAfterLunch;
    private DetailTextView txtBeforeSupper;
    private DetailTextView txtAfterSupper;
    private DetailTextView txtBeforeSleep;


    private FloatingActionButton fab;

//    private LineChartView lineChartView;
//    private List<PointValue> beforeDawnLinePointList = new ArrayList<>();
//    private List<PointValue> beforeSleepLinePointList = new ArrayList<>();
//    private List<PointValue> beforeMealAvgLinePointList = new ArrayList<>();
//    private List<PointValue> afterMealAvgLinePointList = new ArrayList<>();

    private List<AxisValue> xValues;


    private ColumnChartView columnChartView;
    private ColumnChartData columnChartData;

    private BloodSugarRecord record;
    private boolean isDataChange = false;
    private String user_id;

    private List<BloodSugarItemForChart> bloodSugarItemForChartList;
    private List<BloodSugarItem> bloodSugarItemList;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FROM_BSDA:
                if (resultCode == BloodSugarRecordActivity.RESULT_CODE_CHANGE_BSRA) {
                    resetData();
                    isDataChange = true;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_detail);
        MyApplication myApplication = (MyApplication) getApplicationContext();
        user_id = myApplication.getUid();
        initView();


        record = LocalDateBaseHelper.getAllBloodSugarData(user_id);
        bloodSugarItemForChartList = record.getBloodSugarItemForChartList();
        bloodSugarItemList = record.getBloodSugarItemList();


        initChartData();
//        initLineChart();

    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.include);
        toolBar.setFocusable(true);
        toolBar.setFocusableInTouchMode(true);
        toolBar.requestFocus();
        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBar.setTitle("");
        toolBarTitle.setText("血糖");
        setSupportActionBar(toolBar);
//        lineChartView = (LineChartView) findViewById(R.id.line_chart);

        columnChartView = (ColumnChartView) findViewById(R.id.column_chart_view);

        txtDate = (TextView) findViewById(R.id.txt_date);
        txtBeforeDawn = (DetailTextView) findViewById(R.id.txt_before_dawn);
        txtBeforeBreakfast = (DetailTextView) findViewById(R.id.txt_before_breakfast);
        txtAfterBreakfast = (DetailTextView) findViewById(R.id.txt_after_breakfast);
        txtBeforeLunch = (DetailTextView) findViewById(R.id.txt_before_launch);
        txtAfterLunch = (DetailTextView) findViewById(R.id.txt_after_launch);
        txtBeforeSupper = (DetailTextView) findViewById(R.id.txt_before_supper);
        txtAfterSupper = (DetailTextView) findViewById(R.id.txt_after_supper);
        txtBeforeSleep = (DetailTextView) findViewById(R.id.txt_before_sleep);
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToScrollView(scrollView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodSugarDetailActivity.this, BloodSugarRecordActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FROM_BSDA);
            }
        });

    }

    private void initChartData() {
        List<Column> columnList = new ArrayList<>();
        List<SubcolumnValue> values;
        xValues = new ArrayList<>();
        if (bloodSugarItemForChartList != null && bloodSugarItemForChartList.size() != 0) {
            for (int i = 0; i < bloodSugarItemForChartList.size(); i++) {
                values = new ArrayList<>();

                BloodSugarItemForChart item = bloodSugarItemForChartList.get(i);
                xValues.add(new AxisValue(i).setLabel(HelpUtils.getMonthDayInString(item.getDate())));
                values.add(new SubcolumnValue(item.getBeforeDawn(), ContextCompat.getColor(this, R.color.colorAccent)));
                values.add(new SubcolumnValue(item.getBeforeMealAvg(), ContextCompat.getColor(this, R.color.darkgrenn)));
                values.add(new SubcolumnValue(item.getAfterMealAvg(), ContextCompat.getColor(this, R.color.colorPrimaryDark)));
                values.add(new SubcolumnValue(item.getBeforeSleep(), ContextCompat.getColor(this, R.color.purple)));

                Column column = new Column(values);
                column.setHasLabels(false);
                column.setHasLabelsOnlyForSelected(true);
                columnList.add(column);
            }
            columnChartData = new ColumnChartData(columnList);
            columnChartData.setStacked(false);


            //坐标轴
            Axis axisX = new Axis(); //X轴
            axisX.setLineColor(Color.DKGRAY);
            axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
            axisX.setTextColor(Color.DKGRAY);  //设置字体颜色
            axisX.setTextSize(10);//设置字体大小
            axisX.setValues(xValues);  //填充X轴的坐标名称
            columnChartData.setAxisXBottom(axisX); //x 轴在底部
            axisX.setHasLines(false); //x 轴分割线
            axisX.setHasSeparationLine(true);

            // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
            Axis axisY = new Axis();  //Y轴
            axisY.setTextSize(10);//设置字体大小
            columnChartData.setAxisYLeft(axisY);  //Y轴设置在左边
            axisY.setHasLines(true);
            axisY.setLineColor(Color.DKGRAY);
            axisY.setTextColor(Color.DKGRAY);

            columnChartView.setColumnChartData(columnChartData);
            columnChartView.setInteractive(true);
            columnChartView.setValueSelectionEnabled(true);
            int size = bloodSugarItemForChartList.size();
            Viewport v = columnChartView.getMaximumViewport();
            Viewport max = columnChartView.getMaximumViewport();
            if (size > 7) {
                v.left = size - 7;
                v.right = size;
                v.top=v.top+1;
                columnChartView.setCurrentViewport(v);
                max.left = -0.5f;
                max.right = size;
                max.top=max.top+1;
                columnChartView.setMaximumViewport(max);
            } else {
                v.left = -0.5f;
                v.right = 6;
                v.top=v.top+1;

                columnChartView.setCurrentViewport(v);
                max.left = -0.5f;
                max.right = 6;
                max.top=max.top+1;

                columnChartView.setMaximumViewport(max);
            }

            if (size != 0) {
                setDetailOfSpecifiedDay(size - 1);
            }

            columnChartView.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
                @Override
                public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                    setDetailOfSpecifiedDay(columnIndex);
                }

                @Override
                public void onValueDeselected() {

                }
            });
        }

    }


    private void setDetailOfSpecifiedDay(int index) {
        BloodSugarItem item = bloodSugarItemList.get(index);
        txtDate.setText(new java.sql.Date(item.getDate().getTime()).toString());
        txtBeforeDawn.setText(item.getBeforeDawn(), false);
        txtBeforeBreakfast.setText(item.getBeforeBreakfast(), false);
        txtAfterBreakfast.setText(item.getAfterBreakfast(), true);
        txtBeforeLunch.setText(item.getBeforeLunch(), false);
        txtAfterLunch.setText(item.getAfterLunch(), true);
        txtBeforeSleep.setText(item.getBeforeSleep(), false);
        txtBeforeSupper.setText(item.getBeforeSupper(), false);
        txtAfterSupper.setText(item.getAfterSupper(), true);

    }

    private void resetData() {
        record = LocalDateBaseHelper.getAllBloodSugarData(user_id);
        bloodSugarItemForChartList = record.getBloodSugarItemForChartList();
        bloodSugarItemList = record.getBloodSugarItemList();
        initChartData();
//        initLineChart();
    }


    @Override
    public void onBackPressed() {
        if (isDataChange) {
            setResult(RESULT_CODE_CHANGE_BSDA);
            finish();
            super.onBackPressed();
        } else {
            setResult(RESULT_CODE_NO_CHANGE_BSDA);
            finish();
            super.onBackPressed();
        }
    }
}


