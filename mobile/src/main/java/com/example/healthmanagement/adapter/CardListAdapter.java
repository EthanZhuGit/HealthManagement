package com.example.healthmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

import com.example.healthmanagement.MyAxisValueFormatter;
import com.example.healthmanagement.R;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
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

    public interface OnCardClickListener{
        public void onCardClick(View v,String name);
    }

    public void setOnCardClickListener(OnCardClickListener clickListener) {
        this.onCardClickListener=clickListener;
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
    public View getView(final int position, View convertView, @NonNull  ViewGroup parent) {
        Record record = getItem(position);
        final String recordName = record.getName();
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.layout_include_scatter);
        TextView signName = (TextView) view.findViewById(R.id.txt_card_name);
        ImageView signImg = (ImageView) view.findViewById(R.id.img_card_icon);
        TextView latestDetail = (TextView) view.findViewById(R.id.txt_latest_data_detail);
        TextView lastRecordTime = (TextView) view.findViewById(R.id.txt_last_record_date);
        Button recordBtn = (Button) view.findViewById(R.id.btn_record);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onCardClick(v,recordName);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onCardClick(v,recordName);
            }
        });

        if (record != null) {
            switch (record.getName()) {
                case "blood_pressure":
                    Log.d(TAG, "getView: " + "bp");
                    BloodPressureRecord bloodPressureRecord = (BloodPressureRecord) record;
                    signName.setText(record.getName());
                    signImg.setImageResource(R.drawable.ic_blood_pressure);

                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<Float> systolicPressure = new ArrayList<>();
                    ArrayList<Float> diastolicPressure = new ArrayList<>();



//                    List<BloodPressureItem> bloodPressureItemList = bloodPressureRecord.getItemList();
                    List<BloodPressureItem> bloodPressureItemList = bloodPressureRecord.getBloodPressureItemListForChart();


//                    Collections.sort(bloodPressureItemList, new Comparator<BloodPressureItem>() {
//                        @Override
//                        public int compare(BloodPressureItem o1, BloodPressureItem o2) {
//                            if (o1.getDate().after(o2.getDate())) {
//                                return 1;
//                            } else {
//                                return -1;
//                            }
//                        }
//                    });

                    for (BloodPressureItem  b:
                            bloodPressureItemList) {
                        Log.d(TAG,"bp  "+bloodPressureItemList.size()+" "+ b.getDate()+" "+b.getSystolicPressure()+" "+b.getDiastolicPressure());
                    }
                    if (bloodPressureItemList.size()!=0){
                        for (BloodPressureItem item :
                                bloodPressureItemList) {
                            String dateString = new Date(item.getDate().getTime()).toString();
//                            if (dateList.indexOf(dateString) == -1) {
//                                dateList.add(dateString);
//                                systolicPressure.add(item.getSystolicPressure());
//                                diastolicPressure.add(item.getDiastolicPressure());
//                            } else {
//                                int index = dateList.indexOf(dateString);
//                                float oldSPNum = systolicPressure.get(index);
//                                float newSPNum = item.getSystolicPressure();
//                                systolicPressure.set(index, (oldSPNum + newSPNum) / 2);
//                                float oldDPNum = diastolicPressure.get(index);
//                                float newDPNum = item.getDiastolicPressure();
//                                diastolicPressure.set(index, (oldDPNum + newDPNum) / 2);
//                            }
                            dateList.add(dateString);
                            systolicPressure.add(item.getSystolicPressure());
                            diastolicPressure.add(item.getDiastolicPressure());
                        }


                        ScatterChart scatterChart = (ScatterChart) view.findViewById(R.id.scatter_chart);
                        List<Entry> systolicPressureEntryList = convert(systolicPressure);
                        List<Entry> diastolicPressureEntryList = convert(diastolicPressure);

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
                        scatterChart.setData(scatterData);
                        scatterChart.getDescription().setEnabled(false);
                        scatterChart.getLegend().setEnabled(false);
                        scatterChart.setScaleEnabled(false);
                        scatterChart.setTouchEnabled(false);


                        XAxis xAxis = scatterChart.getXAxis();
                        xAxis.setValueFormatter(new MyAxisValueFormatter(dateList));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setAxisMaximum(6.3f);
                        xAxis.setAxisMinimum(-0.3f);
                        xAxis.setAxisLineWidth(2);

                        YAxis yAxis = scatterChart.getAxisLeft();
                        yAxis.setAxisLineWidth(2);
                        YAxis yAxis1 = scatterChart.getAxisRight();
                        yAxis1.setEnabled(false);
                    }


                    break;
                case "blood_sugar":
                    signName.setText(record.getName());
                    signImg.setImageResource(R.drawable.ic_blood_sugar);
                    break;
                case Record.BLOOD_OXYGEN:
                    signName.setText(record.getName());
                    break;
                case Record.HEART_RATE:
                    signName.setText(record.getName());
                    break;
            }
        }
        return view;
    }
}

