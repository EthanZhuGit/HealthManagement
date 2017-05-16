package com.example.healthmanagement.activity;

import android.content.Context;
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
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BloodPressureDetailActivity extends AppCompatActivity {
    private static final String TAG = "TAG" + "BPDetailActivity";

    private LineChart lineChart;
    private TextView txtDate;
    private TextView txtLowBp;
    private TextView txtHighBp;
    private ListView lvDetail;

    private ArrayList<String> dateList;
    private ArrayList<Float> systolicPressure;
    ArrayList<Float> diastolicPressure;


    private DetailAdapter detailAdapter;
    private List<BloodPressureItem> bloodPressureItemList;
    private List<BloodPressureItem> specifiedDayItems = new ArrayList<>();
    Comparator<BloodPressureItem> comparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_detail);
        initView();
        BloodPressureRecord record = LocalDateBaseHelper.getAllBloodPressureData();
        bloodPressureItemList = record.getItemList();
         comparator= new Comparator<BloodPressureItem>() {
            @Override
            public int compare(BloodPressureItem o1, BloodPressureItem o2) {
                if (o1.getDate().after(o2.getDate())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(bloodPressureItemList, comparator);




        AdapterView.OnItemLongClickListener onItemLongClickListener =new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        };
        lvDetail.setOnItemLongClickListener(onItemLongClickListener);


        dateList = new ArrayList<>();
        systolicPressure = new ArrayList<>();
        diastolicPressure = new ArrayList<>();
        if (bloodPressureItemList!=null&&bloodPressureItemList.size() != 0) {
            for (BloodPressureItem item :
                    bloodPressureItemList) {
                String dateString = new Date(item.getDate().getTime()).toString();
                Log.d(TAG, "getView: " + dateList.indexOf(dateString));
                if (dateList.indexOf(dateString) == -1) {
                    dateList.add(dateString);
                    systolicPressure.add(item.getSystolicPressure());
                    diastolicPressure.add(item.getDiastolicPressure());
                } else {
                    int index = dateList.indexOf(dateString);
                    float oldSPNum = systolicPressure.get(index);
                    float newSPNum = item.getSystolicPressure();
                    systolicPressure.set(index, (oldSPNum + newSPNum) / 2);
                    float oldDPNum = diastolicPressure.get(index);
                    float newDPNum = item.getDiastolicPressure();
                    diastolicPressure.set(index, (oldDPNum + newDPNum) / 2);
                }
            }
            List<Entry> systolicPressureEntryList = convert(systolicPressure);
            List<Entry> diastolicPressureEntryList = convert(diastolicPressure);

            LineDataSet systolicPressureDataSet = new LineDataSet(systolicPressureEntryList, "收缩压");
            systolicPressureDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            systolicPressureDataSet.setDrawCircleHole(false);
            systolicPressureDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            systolicPressureDataSet.setValueTextSize(10);
            systolicPressureDataSet.setDrawValues(true);
            systolicPressureDataSet.setValueFormatter(new MyDataSetValueFormatter());
            systolicPressureDataSet.setHighlightLineWidth(2);

            LineDataSet diastolicPressureDataSet = new LineDataSet(diastolicPressureEntryList, "舒张压");
            diastolicPressureDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
            diastolicPressureDataSet.setColor(ContextCompat.getColor(this, R.color.colorAccent));
            diastolicPressureDataSet.setDrawCircleHole(false);
            diastolicPressureDataSet.setValueTextSize(10);
            diastolicPressureDataSet.setDrawValues(true);
            diastolicPressureDataSet.setValueFormatter(new MyDataSetValueFormatter());
            diastolicPressureDataSet.setHighlightLineWidth(2);

            List<ILineDataSet> lineDataSetList = new ArrayList<>();
            lineDataSetList.add(systolicPressureDataSet);
            lineDataSetList.add(diastolicPressureDataSet);

            LineData lineData = new LineData(lineDataSetList);
            lineChart.setData(lineData);
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.setScaleEnabled(false);
            lineChart.setDragEnabled(true);
            lineChart.setVisibleXRangeMaximum(6);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setAxisLineWidth(2);
            xAxis.setAxisMinimum(0);
            xAxis.setValueFormatter(new MyAxisValueFormatter(dateList));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisLineWidth(2);
            YAxis yAxis1 = lineChart.getAxisRight();
            yAxis1.setEnabled(false);


            setDetailOfSpecifiedDay(dateList.size()-1);


            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    lineChart.centerViewTo(e.getX(), e.getY(), YAxis.AxisDependency.LEFT);
                    int index = (int) e.getX();
                    setDetailOfSpecifiedDay(index);
                }
                @Override
                public void onNothingSelected() {}
            });

        }
    }

    private void initView() {
        lineChart = (LineChart) findViewById(R.id.line_chart);
        txtDate = (TextView) findViewById(R.id.txt_date);
        txtLowBp = (TextView) findViewById(R.id.txt_low_bp);
        txtHighBp = (TextView) findViewById(R.id.txt_high_bp);
        lvDetail = (ListView) findViewById(R.id.lv_one_day_detail);
        detailAdapter = new DetailAdapter(this, R.layout.bp_one_day_detail_item, specifiedDayItems);
        lvDetail.setAdapter(detailAdapter);
    }


    private void setDetailOfSpecifiedDay(int index) {
//        lineChart.moveViewToX(index);
        java.util.Date specifiedDay = bloodPressureItemList.get(index).getDate();
        List<BloodPressureItem> result = new ArrayList<>();
        for (int i = 0; i < bloodPressureItemList.size(); i++) {
            BloodPressureItem item = bloodPressureItemList.get(i);
            java.util.Date date = item.getDate();
            if (date.before(LocalDateBaseHelper.getStartTimeOfSpecifiedDaysAgo(specifiedDay, -1)) && date.after(LocalDateBaseHelper.getStartTimeOfSpecifiedDaysAgo(specifiedDay, 0))) {
                result.add(item);
            }
        }
        Collections.sort(result, comparator);
        specifiedDayItems.clear();
        specifiedDayItems.addAll(result);
        detailAdapter.notifyDataSetChanged();
        txtDate.setText(dateList.get(index));
        txtHighBp.setText(String.valueOf(systolicPressure.get(index)));
        txtLowBp.setText(String.valueOf(diastolicPressure.get(index)));
    }

    private List<Entry> convert(List<Float> l) {
        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            entryList.add(new Entry(i, l.get(i)));
        }
        return entryList;
    }
}


class DetailAdapter extends ArrayAdapter<BloodPressureItem> {
    private int resourceId;

    public DetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BloodPressureItem> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BloodPressureItem item = getItem(position);
        View view;
        class ViewHolder{
            TextView txtItemDate;
            TextView txtItemHP;
            TextView txtItemLP;
        }

        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder=new ViewHolder();
            viewHolder.txtItemDate = (TextView) view.findViewById(R.id.txt_bp_detail_date);
            viewHolder.txtItemHP=(TextView) view.findViewById(R.id.txt_bp_detail_high);
            viewHolder.txtItemLP = (TextView) view.findViewById(R.id.txt_bp_detail_low);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder = ((ViewHolder) view.getTag());
        }
        float high=item.getSystolicPressure();
        float low=item.getDiastolicPressure();
        viewHolder.txtItemDate.setText(HelpUtils.getTimeWithoutSecInString(item.getDate()));
        viewHolder.txtItemHP.setText(String.valueOf(item.getSystolicPressure()));
        viewHolder.txtItemLP.setText(String.valueOf(item.getDiastolicPressure()));
        if (high < 90) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low));
        } else if (high < 140) {

        } else if (high < 160) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.one));
        } else if (high < 180) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.two));
        } else {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.three));
        }
        return view;
    }
}

class MyDataSetValueFormatter implements IValueFormatter {
    private DecimalFormat format;

    public MyDataSetValueFormatter() {
        format = new DecimalFormat("###.#");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return format.format(value);
    }
}
