package com.example.healthmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.MyAxisValueFormatter;
import com.example.healthmanagement.R;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.example.healthmanagement.model.BloodSugarItemForChart;
import com.example.healthmanagement.model.BloodSugarRecord;
import com.example.healthmanagement.model.Record;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/14 0014.
 */


public class CardListAdapter extends ArrayAdapter<Record> {
    private static final String TAG = "TAG" + "CardListAdapter";
    private int resourceId;
    private OnCardClickListener onCardClickListener;

    public interface OnCardClickListener {
        public void onCardClick(View v, String name);
    }

    public void setOnCardClickListener(OnCardClickListener clickListener) {
        this.onCardClickListener = clickListener;
    }

    public CardListAdapter(Context context, int textViewResourceId, List<Record> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    private List<Entry> convert(List<Float> l) {
        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            entryList.add(new Entry(i, l.get(i)));
        }
        return entryList;
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        Record record = getItem(position);
        final String recordName = record.getName();

        class ViewHolderScatter {
            RelativeLayout relativeLayout;
            TextView signName;
            ImageView signImg;
            TextView latestDetail;
            TextView lastRecordTime;
            Button recordBtn;
            ScatterChart scatterChart;
        }
        ViewHolderScatter viewHolderScatter;
        View view;
        if (convertView == null) {
            viewHolderScatter = new ViewHolderScatter();
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolderScatter.relativeLayout = (RelativeLayout) view.findViewById(R.id.layout_include_scatter);
            viewHolderScatter.signName = (TextView) view.findViewById(R.id.txt_card_name);
            viewHolderScatter.signImg = (ImageView) view.findViewById(R.id.img_card_icon);
            viewHolderScatter.latestDetail = (TextView) view.findViewById(R.id.txt_latest_data_detail);

            viewHolderScatter.lastRecordTime = (TextView) view.findViewById(R.id.txt_last_record_date);

            viewHolderScatter.recordBtn = (Button) view.findViewById(R.id.btn_record);
            viewHolderScatter.scatterChart = (ScatterChart) view.findViewById(R.id.scatter_chart);
            view.setTag(viewHolderScatter);
        } else {
            view = convertView;
            viewHolderScatter = (ViewHolderScatter) view.getTag();
        }

        viewHolderScatter.recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onCardClick(v, recordName);
            }
        });

        viewHolderScatter.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onCardClick(v, recordName);
            }
        });

        viewHolderScatter.scatterChart.clear();           //清空残留数据
        viewHolderScatter.lastRecordTime.setText("");
        viewHolderScatter.latestDetail.setText("");
        viewHolderScatter.scatterChart.getDescription().setEnabled(false);
        viewHolderScatter.scatterChart.getLegend().setEnabled(false);
        viewHolderScatter.scatterChart.setScaleEnabled(false);
        viewHolderScatter.scatterChart.setTouchEnabled(false);

        switch (record.getName()) {
            case "blood_pressure":
                BloodPressureRecord bloodPressureRecord = (BloodPressureRecord) record;
                viewHolderScatter.signName.setText("血压");
                viewHolderScatter.signImg.setImageResource(R.drawable.ic_blood_pressure);
                viewHolderScatter.scatterChart.setNoDataText("无记录");
                List<BloodPressureItem> bloodPressureItemList = bloodPressureRecord.getBloodPressureItemListForChart();

                if (bloodPressureItemList.size() != 0) {
                    ArrayList<String> dateList = new ArrayList<>();
                    List<Entry> systolicPressureEntryList = new ArrayList<>();
                    List<Entry> diastolicPressureEntryList = new ArrayList<>();

                    for (int i = 0; i < bloodPressureItemList.size(); i++) {
                        BloodPressureItem item = bloodPressureItemList.get(i);
                        String dateString = new Date(item.getDate().getTime()).toString();
                        dateList.add(dateString);
                        float systolic = item.getSystolicPressure();
                        float diastolic = item.getDiastolicPressure();
                        Log.d(TAG, "getView: " + dateString + " " + systolic + " " + diastolic);
                        systolicPressureEntryList.add(new Entry(i, systolic));
                        diastolicPressureEntryList.add(new Entry(i, diastolic));
                        if (i == bloodPressureItemList.size() - 1) {
                            viewHolderScatter.lastRecordTime.setText(HelpUtils.getMonthDayInString(item.getDate()));
                            StringBuffer buffer = new StringBuffer();
                            if (diastolic < 60) {
                                buffer.append("舒张压:" + diastolic + "偏低  ");
                            } else if (diastolic < 90) {
                                buffer.append("舒张压:" + diastolic + "正常  ");
                            } else if (diastolic < 100) {
                                buffer.append("舒张压:" + diastolic + "过高  ");
                            } else {
                                buffer.append("舒张压:" + diastolic + "危险  ");
                            }
                            if (systolic < 90) {
                                buffer.append("收缩压:" + systolic + " 偏低");
                            } else if (systolic < 140) {
                                buffer.append("收缩压:" + systolic + " 正常");
                            } else if (systolic < 160) {
                                buffer.append("收缩压:" + systolic + " 过高");
                            } else {
                                buffer.append("收缩压:" + systolic + " 危险");
                            }

                            viewHolderScatter.latestDetail.setText(buffer.toString());
                        }

                    }


                    ScatterDataSet systolicPressureDataSet = new ScatterDataSet(systolicPressureEntryList, "收缩压");
                    systolicPressureDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    systolicPressureDataSet.setScatterShapeSize(22);
                    systolicPressureDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    systolicPressureDataSet.setValueTextSize(10);
                    systolicPressureDataSet.setDrawValues(false);

                    ScatterDataSet diastolicPressureDataSet = new ScatterDataSet(diastolicPressureEntryList, "舒张压");
                    diastolicPressureDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    diastolicPressureDataSet.setScatterShapeSize(20);
                    diastolicPressureDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    diastolicPressureDataSet.setDrawValues(false);

                    List<IScatterDataSet> scatterDataSetList = new ArrayList<>();
                    scatterDataSetList.add(systolicPressureDataSet);
                    scatterDataSetList.add(diastolicPressureDataSet);
                    ScatterData scatterData = new ScatterData(scatterDataSetList);
                    viewHolderScatter.scatterChart.setData(scatterData);

                    XAxis xAxis = viewHolderScatter.scatterChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setAxisLineWidth(2);
                    YAxis yAxis = viewHolderScatter.scatterChart.getAxisLeft();
                    yAxis.setAxisLineWidth(2);
                    YAxis yAxis1 = viewHolderScatter.scatterChart.getAxisRight();
                    yAxis1.setEnabled(false);

                    xAxis.setValueFormatter(new MyAxisValueFormatter(dateList));
                    xAxis.setAxisMaximum(6.3f);
                    xAxis.setAxisMinimum(-0.3f);
                    yAxis.resetAxisMaximum();
                    yAxis.resetAxisMinimum();
                    Log.d(TAG, "getView: " + "bp");
                }
                break;
            case "blood_sugar":
                viewHolderScatter.signName.setText("血糖");
                viewHolderScatter.signImg.setImageResource(R.drawable.ic_blood_sugar);
                viewHolderScatter.scatterChart.setNoDataText("无记录");
                BloodSugarRecord bloodSugarRecord = (BloodSugarRecord) record;
                List<BloodSugarItemForChart> itemForChartList = bloodSugarRecord.getBloodSugarItemForChartList();

                if (itemForChartList != null && itemForChartList.size() != 0) {
                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<Entry> beforeMealAvg = new ArrayList<>();
                    ArrayList<Entry> afterMealAvg = new ArrayList<>();
                    ArrayList<Entry> beforeDawn = new ArrayList<>();
                    ArrayList<Entry> beforeSleep = new ArrayList<>();
                    for (int i = 0; i < itemForChartList.size(); i++) {
                        BloodSugarItemForChart item = itemForChartList.get(i);
                        String dateString = new Date(item.getDate().getTime()).toString();
                        dateList.add(dateString);
                        float beforeMe = item.getBeforeMealAvg();
                        float afterMe = item.getAfterMealAvg();
                        float beforeDa = item.getBeforeDawn();
                        float beforeSl = item.getBeforeSleep();
                        beforeMealAvg.add(new Entry(i, beforeMe));
                        afterMealAvg.add(new Entry(i, afterMe));
                        beforeDawn.add(new Entry(i, beforeDa));
                        beforeSleep.add(new Entry(i, beforeSl));

                        if (i == itemForChartList.size() - 1) {
                            viewHolderScatter.lastRecordTime.setText(HelpUtils.getMonthDayInString(item.getDate()));
                            StringBuffer buffer = new StringBuffer();
                            if (beforeDa == 0) {
                                buffer.append("");
                            } else if (beforeDa < 3) {
                                buffer.append("凌晨:" + beforeDa + "偏低  ");
                            } else if (beforeDa < 6.1) {
                                buffer.append("凌晨:" + beforeDa + "正常  ");
                            } else if (beforeDa < 7.0) {
                                buffer.append("凌晨:" + beforeDa + "过高  ");
                            } else {
                                buffer.append("凌晨:" + beforeDa + "危险  ");
                            }
                            if (beforeMe == 0) {
                                buffer.append("");
                            } else if (beforeMe < 3) {
                                buffer.append("餐前:" + beforeMe + " 偏低  ");
                            } else if (beforeMe < 6.1) {
                                buffer.append("餐前:" + beforeMe + " 正常  ");
                            } else if (beforeMe < 7.0) {
                                buffer.append("餐前:" + beforeMe + " 过高  ");
                            } else {
                                buffer.append("餐前:" + beforeMe + " 危险  ");
                            }

                            if (afterMe == 0) {
                                buffer.append("");
                            } else if (afterMe < 3) {
                                buffer.append("餐后:" + afterMe + "偏低  ");
                            } else if (afterMe < 7.8) {
                                buffer.append("餐后:" + afterMe + "正常  ");
                            } else if (afterMe < 11.1) {
                                buffer.append("餐后:" + afterMe + "过高  ");
                            } else {
                                buffer.append("餐后:" + afterMe + "危险  ");
                            }

                            if (beforeSl == 0) {
                                buffer.append("");
                            } else if (beforeSl < 3) {
                                buffer.append("餐前:" + beforeSl + " 偏低");
                            } else if (beforeSl < 6.1) {
                                buffer.append("餐前:" + beforeSl + " 正常");
                            } else if (beforeSl < 7.0) {
                                buffer.append("餐前:" + beforeSl + " 过高");
                            } else {
                                buffer.append("餐前:" + beforeSl + " 危险");
                            }

                            viewHolderScatter.latestDetail.setText(buffer.toString());
                        }

                    }

                    ScatterDataSet beforeMealAvgDataSet = new ScatterDataSet(beforeMealAvg, "餐前均值");
                    beforeMealAvgDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    beforeMealAvgDataSet.setScatterShapeSize(22);
                    beforeMealAvgDataSet.setColor(ContextCompat.getColor(getContext(), R.color.dark));
                    beforeMealAvgDataSet.setValueTextSize(10);
                    beforeMealAvgDataSet.setDrawValues(false);
                    ScatterDataSet afterMealAvgDataSet = new ScatterDataSet(afterMealAvg, "餐后均值");
                    afterMealAvgDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    afterMealAvgDataSet.setScatterShapeSize(22);
                    afterMealAvgDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    afterMealAvgDataSet.setValueTextSize(10);
                    afterMealAvgDataSet.setDrawValues(false);
                    ScatterDataSet beforeDawnDataSet = new ScatterDataSet(beforeDawn, "凌晨");
                    beforeDawnDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    beforeDawnDataSet.setScatterShapeSize(22);
                    beforeDawnDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    beforeDawnDataSet.setValueTextSize(10);
                    beforeDawnDataSet.setDrawValues(false);
                    ScatterDataSet beforeSleepDataSet = new ScatterDataSet(beforeSleep, "睡前");
                    beforeSleepDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    beforeSleepDataSet.setScatterShapeSize(22);
                    beforeSleepDataSet.setColor(ContextCompat.getColor(getContext(), R.color.purple));
                    beforeSleepDataSet.setValueTextSize(10);
                    beforeSleepDataSet.setDrawValues(false);

                    List<IScatterDataSet> scatterDataSetList = new ArrayList<>();
                    scatterDataSetList.add(beforeDawnDataSet);
                    scatterDataSetList.add(afterMealAvgDataSet);
                    scatterDataSetList.add(beforeMealAvgDataSet);
                    scatterDataSetList.add(beforeSleepDataSet);
                    ScatterData scatterData = new ScatterData(scatterDataSetList);
                    viewHolderScatter.scatterChart.setData(scatterData);

                    XAxis xAxis = viewHolderScatter.scatterChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setAxisLineWidth(2);
                    YAxis yAxisRight = viewHolderScatter.scatterChart.getAxisRight();
                    yAxisRight.setEnabled(false);
                    YAxis yAxisLeft = viewHolderScatter.scatterChart.getAxisLeft();
                    yAxisLeft.setAxisLineWidth(2);

                    xAxis.setValueFormatter(new MyAxisValueFormatter(dateList));
                    xAxis.setAxisMaximum(6.3f);
                    xAxis.setAxisMinimum(-0.5f);
                    yAxisLeft.resetAxisMinimum();
                    yAxisLeft.resetAxisMaximum();

                    yAxisLeft.setAxisMinimum(3.0f);
                    yAxisLeft.setAxisMaximum(15.0f);

                }
                break;

            case Record.BLOOD_OXYGEN:
                viewHolderScatter.signName.setText("血氧");
                viewHolderScatter.scatterChart.setNoDataText("无记录");
                break;
            case Record.HEART_RATE:
                viewHolderScatter.signName.setText("心率");
                viewHolderScatter.scatterChart.setNoDataText("无记录");
                break;
        }
        return view;
    }
}

