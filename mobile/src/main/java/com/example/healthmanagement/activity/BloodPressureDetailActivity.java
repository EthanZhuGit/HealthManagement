package com.example.healthmanagement.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.MyAxisValueFormatter;
import com.example.healthmanagement.R;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;


import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class BloodPressureDetailActivity extends AppCompatActivity {
    private static final String TAG = "TAG" + "BPDetailActivity";

//    private LineChart lineChart;

    private TextView txtDate;
    private TextView txtLowBp;
    private TextView txtHighBp;
    private ListView lvDetail;

    private LineChartView lineChartView;
    private List<PointValue> highLinePointList = new ArrayList<>();
    private List<PointValue> lowLinePointList = new ArrayList<>();
    private List<AxisValue> xValues = new ArrayList<>();


    private ArrayList<String> dateList;
    private ArrayList<Float> systolicPressure;
    ArrayList<Float> diastolicPressure;
    private BloodPressureRecord record;

    private DetailAdapter detailAdapter;
    private List<BloodPressureItem> bloodPressureItemList;
    private List<BloodPressureItem> specifiedDayItems = new ArrayList<>();
    Comparator<BloodPressureItem> comparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_detail);
        initView();
        record = LocalDateBaseHelper.getAllBloodPressureData();

        bloodPressureItemList = record.getBloodPressureItemListForChart();


        initChartData();
        initLineChart();

        AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        };
        lvDetail.setOnItemLongClickListener(onItemLongClickListener);
    }

    private void initView() {
        lineChartView = (LineChartView) findViewById(R.id.line_chart);
//        lineChart = (LineChart) findViewById(R.id.line_chart);
        txtDate = (TextView) findViewById(R.id.txt_date);
        txtLowBp = (TextView) findViewById(R.id.txt_low_bp);
        txtHighBp = (TextView) findViewById(R.id.txt_high_bp);
        lvDetail = (ListView) findViewById(R.id.lv_one_day_detail);
        detailAdapter = new DetailAdapter(this, R.layout.bp_one_day_detail_item, specifiedDayItems);
        lvDetail.setAdapter(detailAdapter);
    }


    private void setDetailOfSpecifiedDay(int index) {
        BloodPressureItem item = bloodPressureItemList.get(index);
        java.util.Date specifiedDay = item.getDate();
        HashMap<java.util.Date, List<BloodPressureItem>> map = record.getItemGroupedByCalendar();
        List<BloodPressureItem> result = map.get(specifiedDay);
        specifiedDayItems.clear();
        specifiedDayItems.addAll(result);
        detailAdapter.notifyDataSetChanged();
        txtDate.setText(new Date(item.getDate().getTime()).toString());
        txtHighBp.setText(String.valueOf(item.getSystolicPressure()));
        txtLowBp.setText(String.valueOf(item.getDiastolicPressure()));

    }

    private void initChartData() {
        if (bloodPressureItemList != null && bloodPressureItemList.size() != 0) {
            for (int i = 0; i < bloodPressureItemList.size(); i++) {
                BloodPressureItem item = bloodPressureItemList.get(i);
                xValues.add(new AxisValue(i).setLabel(HelpUtils.getMonthDayInString(item.getDate())));
                highLinePointList.add(new PointValue(i, item.getSystolicPressure()));
                lowLinePointList.add(new PointValue(i, item.getDiastolicPressure()));
            }
        }
    }

    private void initLineChart() {
        Line lowLine = new Line(lowLinePointList).setColor(ContextCompat.getColor(this, R.color.colorAccent));  //折线的颜色
        lowLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        lowLine.setCubic(false);//曲线是否平滑，即是曲线还是折线
        lowLine.setFilled(false);//是否填充曲线的面积
        lowLine.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lowLine.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lowLine.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lowLine.setStrokeWidth(1);


        Line highLine = new Line(highLinePointList).setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));  //折线的颜色
        highLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        highLine.setCubic(false);//曲线是否平滑，即是曲线还是折线
        highLine.setFilled(false);//是否填充曲线的面积
        highLine.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        highLine.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        highLine.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        highLine.setStrokeWidth(1);

        List<Line> lines = new ArrayList<>();
        lines.add(lowLine);
        lines.add(highLine);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setLineColor(Color.BLACK);
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(xValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        axisX.setHasSeparationLine(true);

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setHasLines(true);
        axisY.setLineColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);


        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomEnabled(false);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
        lineChartView.setValueSelectionEnabled(true);

        /*
         * 注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        int size = bloodPressureItemList.size();
        if (size > 7) {
            Viewport v = new Viewport(lineChartView.getMaximumViewport());
            v.right = size;
            v.left = size - 7;
            lineChartView.setCurrentViewport(v);
        }

        setDetailOfSpecifiedDay(size-1);

        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Log.d(TAG, "onValueSelected: " + pointIndex);
                setDetailOfSpecifiedDay(pointIndex);
                Viewport v = new Viewport(lineChartView.getMaximumViewport());
                v.right = pointIndex+3;
                v.left = pointIndex-3;
                lineChartView.setCurrentViewport(v);
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }
}


class DetailAdapter extends ArrayAdapter<BloodPressureItem> {
    private int resourceId;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BloodPressureItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BloodPressureItem item = getItem(position);
        View view;
        class ViewHolder {
            TextView txtItemDate;
            TextView txtItemHP;
            TextView txtItemLP;
        }

        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.txtItemDate = (TextView) view.findViewById(R.id.txt_bp_detail_date);
            viewHolder.txtItemHP = (TextView) view.findViewById(R.id.txt_bp_detail_high);
            viewHolder.txtItemLP = (TextView) view.findViewById(R.id.txt_bp_detail_low);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }
        float high = item.getSystolicPressure();
        float low = item.getDiastolicPressure();
        viewHolder.txtItemDate.setText(HelpUtils.getTimeWithoutSecInString(item.getDate()));
        viewHolder.txtItemHP.setText(String.valueOf(high));
        viewHolder.txtItemLP.setText(String.valueOf(low));
        if (high < 90) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low));
        } else if (high < 140) {
            viewHolder.txtItemHP.setBackgroundColor(Color.WHITE);
        } else if (high < 160) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.one));
        } else if (high < 180) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.two));
        } else {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.three));
        }

        if (low < 60) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low));
        } else if (low < 90) {
            viewHolder.txtItemLP.setBackgroundColor(Color.WHITE);
        } else if (low < 100) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.one));
        } else if (low < 110) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.two));
        } else {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.three));
        }

        return view;
    }
}

