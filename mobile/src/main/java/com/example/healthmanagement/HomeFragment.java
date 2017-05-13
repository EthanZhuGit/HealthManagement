package com.example.healthmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.Record;
import com.example.healthmanagement.model.User;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "TAG" + "HomeFragment";
    private static final String USER_CLOUD_ID = "user_cloud_id";

    private ListView cardListView;
    private CardListAdapter cardListAdapter;
    private String userCloudId;
    private Record bloodPressureRecord;
    private List<Record> records = new ArrayList<>();
    private List<BloodPressureItem> bloodPressureItemListTest = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String s) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_CLOUD_ID, s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userCloudId = getArguments().getString(USER_CLOUD_ID);
            Log.d(TAG, "onCreate: " + userCloudId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cardListView = (ListView) view.findViewById(R.id.card_list);

        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494397812000L), 115.0f, 80.0f));
        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494292745000L), 130.0f, 100.0f));
        bloodPressureItemListTest.add(new BloodPressureItem(new java.util.Date(1494292749000L), 125.0f, 92.0f));
        DataSupport.saveAll(bloodPressureItemListTest);

        User user = new User();
        user.setUsername("朱一新");
        user.setPhoneNum("18086742831");
        user.setAge(22);
        user.setSex("男");
        user.setBloodPressureItemList(bloodPressureItemListTest);
        user.save();

        BloodPressureItem item = new BloodPressureItem(new java.util.Date(), 150.0f, 90.0f);
        item.save();
        user.getBloodPressureItemList().add(item);
        user.saveOrUpdate("phonenum=?", "18086742831");



        bloodPressureRecord = new Record("blood_pressure", bloodPressureItemListTest, null);
        records.add(bloodPressureRecord);
        cardListAdapter = new CardListAdapter(getContext(), R.layout.card_item, records);
        cardListView.setAdapter(cardListAdapter);
        return view;
    }

}


class CardListAdapter extends ArrayAdapter<Record> {
    private static final String TAG = "TAG" + "CardListAdapter";
    private int resourceId;

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

    public View getView(final int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView signName = (TextView) view.findViewById(R.id.txt_card_name);
        ImageView signImg = (ImageView) view.findViewById(R.id.img_card_icon);
        TextView latestDetail = (TextView) view.findViewById(R.id.txt_latest_data_detail);
        TextView lastRecordTime = (TextView) view.findViewById(R.id.txt_last_record_date);
        if (record != null) {
            switch (record.getName()) {
                case "blood_pressure":
                    signName.setText(record.getName());
                    signImg.setImageResource(R.drawable.ic_blood_pressure);

                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<Float> systolicPressure = new ArrayList<>();
                    ArrayList<Float> diastolicPressure = new ArrayList<>();

                    /*
                    Set<java.util.Date> keys=record.getBloodPressureDataMap().keySet();
                    ArrayList<java.util.Date> d = new ArrayList<>();
                    d.addAll(keys);
                    Collections.sort(d, new Comparator<java.util.Date>() {
                        @Override
                        public int compare(java.util.Date o1, java.util.Date o2) {
                            if (o1.after(o2)) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    Log.d(TAG, "getView: " + Arrays.toString(d.toArray()));

                    ArrayList<String> dateList=new ArrayList<>();
                    ArrayList<Float> systolicPressure = new ArrayList<>();
                    ArrayList<Float> diastolicPressure = new ArrayList<>();

                    for (java.util.Date utilDate : d) {
                        Log.d(TAG, "getView: "+utilDate.toString());
                        BloodPressureItem item=record.getBloodPressureDataMap().get(utilDate);
                        String dateString=new Date(utilDate.getTime()).toString();
                        Log.d(TAG, "getView: "+dateList.indexOf(dateString));
                        if (dateList.indexOf(dateString) == -1) {
                            dateList.add(dateString);
                            systolicPressure.add(item.getSystolicPpressure());
                            diastolicPressure.add(item.getDiastolicPressure());
                        } else {
                            int index = dateList.indexOf(dateString);
                            float oldSPNum = systolicPressure.get(index);
                            float newSPNum = item.getSystolicPpressure();
                            systolicPressure.set(index, (oldSPNum + newSPNum) / 2);
                            float oldDPNum = diastolicPressure.get(index);
                            float newDPNum = item.getDiastolicPressure();
                            diastolicPressure.set(index, (oldDPNum + newDPNum) / 2);
                        }
                    }
                    */

                    List<BloodPressureItem> bloodPressureItemList = record.getBloodPressureItemList();
                    Collections.sort(bloodPressureItemList, new Comparator<BloodPressureItem>() {
                        @Override
                        public int compare(BloodPressureItem o1, BloodPressureItem o2) {
                            if (o1.getDate().after(o2.getDate())) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    for (BloodPressureItem item :
                            bloodPressureItemList) {
                        String dateString = new Date(item.getDate().getTime()).toString();
                        Log.d(TAG, "getView: " + dateList.indexOf(dateString));
                        if (dateList.indexOf(dateString) == -1) {
                            dateList.add(dateString);
                            systolicPressure.add(item.getSystolicPpressure());
                            diastolicPressure.add(item.getDiastolicPressure());
                        } else {
                            int index = dateList.indexOf(dateString);
                            float oldSPNum = systolicPressure.get(index);
                            float newSPNum = item.getSystolicPpressure();
                            systolicPressure.set(index, (oldSPNum + newSPNum) / 2);
                            float oldDPNum = diastolicPressure.get(index);
                            float newDPNum = item.getDiastolicPressure();
                            diastolicPressure.set(index, (oldDPNum + newDPNum) / 2);
                        }
                    }


                    ScatterChart scatterChart = (ScatterChart) view.findViewById(R.id.scatter_chart);
                    List<Entry> systolicPressureEntryList = convert(systolicPressure);
                    List<Entry> diastolicPressureEntryList = convert(diastolicPressure);

                    ScatterDataSet systolicPressureDataSet = new ScatterDataSet(systolicPressureEntryList, "收缩压");
                    systolicPressureDataSet.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);
                    systolicPressureDataSet.setScatterShapeSize(22);
                    systolicPressureDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    systolicPressureDataSet.setValueTextSize(10);
                    systolicPressureDataSet.setDrawValues(false);

                    ScatterDataSet diastolicPressureDataSet = new ScatterDataSet(diastolicPressureEntryList, "舒张压");
                    diastolicPressureDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    diastolicPressureDataSet.setScatterShapeSize(20);
                    diastolicPressureDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
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
                    xAxis.setValueFormatter(new XF(dateList));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setAxisMaximum(7.3f);
                    xAxis.setAxisMinimum(-0.3f);
                    xAxis.setAxisLineWidth(2);

                    YAxis yAxis = scatterChart.getAxisLeft();
                    yAxis.setAxisLineWidth(2);
                    YAxis yAxis1 = scatterChart.getAxisRight();
                    yAxis1.setEnabled(false);

                    break;
                case "blood_sugar":
                    signName.setText(record.getName());
                    signImg.setImageResource(R.drawable.ic_blood_sugar);
                    break;
            }
        }


        return view;
    }

    class XF implements IAxisValueFormatter {
        private ArrayList<String> values;

        public XF(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if ((int) value < values.size()) {
                String s = values.get((int) value);
                SimpleDateFormat output = new SimpleDateFormat("M/d");
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = null;
                try {
                    date = input.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return output.format(date);
            } else {
                return " ";
            }

        }
    }

}

