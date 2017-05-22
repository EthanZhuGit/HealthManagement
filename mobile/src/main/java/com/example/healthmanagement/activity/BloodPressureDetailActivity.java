package com.example.healthmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthmanagement.HelpUtils;
import com.example.healthmanagement.customview.ListViewForScrollView;
import com.example.healthmanagement.MyApplication;
import com.example.healthmanagement.R;
import com.example.healthmanagement.datebase.LocalDateBaseHelper;
import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodPressureRecord;
import com.example.healthmanagement.model.DeletedItems;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;


import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
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
    public static final int REQUEST_CODE_FROM_BPDA = 1000;
    public static final int RESULT_CODE_CHANGE_BPDA = 1001;
    public static final int RESULT_CODE_NO_CHANGE_BPDA = 1003;

//    private LineChart lineChart;

    private TextView txtDate;
    private TextView txtLowBp;
    private TextView txtHighBp;
    private ListViewForScrollView lvDetail;
    private FloatingActionButton fab;

    private LineChartView lineChartView;
    private List<PointValue> highLinePointList = new ArrayList<>();
    private List<PointValue> lowLinePointList = new ArrayList<>();
    private List<AxisValue> xValues = new ArrayList<>();

    private BloodPressureRecord record;

    private MyDetailAdapter detailAdapter;

    private List<BloodPressureItem> bloodPressureItemList;
    private List<BloodPressureItem> specifiedDayItems = new ArrayList<>();

    private List<ItemForAdapter> itemForAdapterList = new ArrayList<>();

    Comparator<BloodPressureItem> comparator;

    private boolean isDataChange = false;

    private boolean isMultiChoose = false;

    private String user_id;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_FROM_BPDA:
                if (resultCode == BloodPressureRecordActivity.RESULT_CODE_CHANGE_BPRA) {
                    resetData();
                    isDataChange = true;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_detail);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        user_id = myApplication.getUid();

        initView();

        record = LocalDateBaseHelper.getAllBloodPressureData(user_id);
        bloodPressureItemList = record.getBloodPressureItemListForChart();
        detailAdapter = new MyDetailAdapter(this, itemForAdapterList);
        lvDetail.setAdapter(detailAdapter);

        initChartData();
        initLineChart();


        MyDetailAdapter.OnDetailItemClickListener onDetailItemClickListener = new MyDetailAdapter.OnDetailItemClickListener() {
            @Override
            public void onDetailItemClick(int position, boolean isChecked) {
                itemForAdapterList.get(position).setChecked(isChecked);
            }
        };
        detailAdapter.setOnDetailItemClickListener(onDetailItemClickListener);


        lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + position);
            }
        });

        lvDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: " + position);
                lvDetail.setLongClickable(false);
                isMultiChoose = true;
                fab.setImageResource(R.drawable.ic_delete);
                for (ItemForAdapter i :
                        itemForAdapterList) {
                    i.setMultiChoose(isMultiChoose);
                    i.setChecked(false);
                }
                detailAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void initView() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.include);
        toolBar.setFocusable(true);
        toolBar.setFocusableInTouchMode(true);
        toolBar.requestFocus();
        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBar.setTitle("");
        toolBarTitle.setText("血压");
        setSupportActionBar(toolBar);
        lineChartView = (LineChartView) findViewById(R.id.line_chart);
        txtDate = (TextView) findViewById(R.id.txt_date);
        txtLowBp = (TextView) findViewById(R.id.txt_low_bp);
        txtHighBp = (TextView) findViewById(R.id.txt_high_bp);
        lvDetail = (ListViewForScrollView) findViewById(R.id.lv_one_day_detail);


        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToScrollView(scrollView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + "fab");
                if (isMultiChoose) {
                    if (itemForAdapterList != null && itemForAdapterList.size() != 0) {
                        int num = 0;
                        for (int i = 0; i < itemForAdapterList.size(); i++) {
                            if (itemForAdapterList.get(i).isChecked()) {
                                String date = String.valueOf(specifiedDayItems.get(i).getDate().getTime());
                                Log.d(TAG, "onClick: " + date + " " + specifiedDayItems.get(i).getDate() + " " + user_id);
                                List<BloodPressureItem> deleteList = DataSupport.where("date =? and user_id=?", date, user_id).find(BloodPressureItem.class);
//                                DataSupport.deleteAll(BloodPressureItem.class, "date =? and user_id=?", date, user_id);
                                BloodPressureItem item = deleteList.get(0);
                                String object_id = item.getObject_id();
                                if (object_id != null && object_id.trim().length() != 0) {
                                    DeletedItems deletedItems = new DeletedItems();
                                    deletedItems.setTableNameOfItem("bloodpressureitem");
                                    deletedItems.setObjectIdOfItem(object_id);
                                    deletedItems.save();
                                    Log.d(TAG, "onClick: " + "删除表增加"+" bp "+object_id);
                                }
                                item.delete();
                                num++;
                            }
                        }
                        if (num == 0) {
                            Toast.makeText(BloodPressureDetailActivity.this, "无选择项", Toast.LENGTH_SHORT).show();
                        } else if (num == itemForAdapterList.size()) {
                            isMultiChoose = false;
                            itemForAdapterList.clear();
                            detailAdapter.notifyDataSetChanged();
                            txtDate.setText("");
                            txtLowBp.setText("0");
                            txtHighBp.setText("0");
                            lvDetail.setLongClickable(true);
                            resetData();
                            isDataChange = true;
                            fab.setImageResource(R.drawable.ic_add_white_24dp);
                        } else {
                            isMultiChoose = false;
                            lvDetail.setLongClickable(true);
                            resetData();
                            isDataChange = true;
                            fab.setImageResource(R.drawable.ic_add_white_24dp);
                        }
                    }
                } else {
                    Intent intent = new Intent(BloodPressureDetailActivity.this, BloodPressureRecordActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_FROM_BPDA);
                }

            }
        });
    }


    private void setDetailOfSpecifiedDay(int index) {
        BloodPressureItem item = bloodPressureItemList.get(index);
        java.util.Date specifiedDay = item.getDate();
        HashMap<java.util.Date, List<BloodPressureItem>> map = record.getItemGroupedByCalendar();
        List<BloodPressureItem> result = map.get(specifiedDay);
        specifiedDayItems.clear();
        specifiedDayItems.addAll(result);
        itemForAdapterList.clear();
        for (int i = 0; i < specifiedDayItems.size(); i++) {
            itemForAdapterList.add(new ItemForAdapter(isMultiChoose, false, specifiedDayItems.get(i)));
        }
        detailAdapter.notifyDataSetChanged();
        txtDate.setText(new Date(item.getDate().getTime()).toString());
        txtHighBp.setText(String.valueOf(item.getSystolicPressure()));
        txtLowBp.setText(String.valueOf(item.getDiastolicPressure()));

    }

    private void initChartData() {
        xValues.clear();
        highLinePointList.clear();
        lowLinePointList.clear();
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
        Line lowLine = new Line(lowLinePointList);
        lowLine.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        lowLine.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        lowLine.setCubic(false);//曲线是否平滑，即是曲线还是折线
        lowLine.setFilled(false);//是否填充曲线的面积
        lowLine.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lowLine.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        lowLine.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lowLine.setStrokeWidth(1);


        Line highLine = new Line(highLinePointList);
        highLine.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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
        axisX.setLineColor(Color.DKGRAY);
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.DKGRAY);  //设置字体颜色
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
        axisY.setLineColor(Color.DKGRAY);
        axisY.setTextColor(Color.DKGRAY);


        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setZoomEnabled(true);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
        lineChartView.setValueSelectionEnabled(true);

        /*
         * 注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        int size = bloodPressureItemList.size();
        Viewport v = lineChartView.getMaximumViewport();
        Viewport max = lineChartView.getMaximumViewport();
        if (size > 7) {
//            v = initViewPort(size - 7, size);
            v.left = size - 7;
            v.right = size;
            lineChartView.setCurrentViewport(v);
            max.left = 0;
            max.right = size;
            lineChartView.setMaximumViewport(max);
        } else {
//            v = initViewPort(0, 7);
            v.left = 0;
            v.right = 6;
            lineChartView.setCurrentViewport(v);
            max.left = 0;
            max.right = 6;
            lineChartView.setMaximumViewport(max);
        }

        if (size != 0) {
            setDetailOfSpecifiedDay(size - 1);
        }


        lineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Log.d(TAG, "onValueSelected: " + pointIndex);
                setDetailOfSpecifiedDay(pointIndex);
                Viewport v = new Viewport(lineChartView.getMaximumViewport());
                v.right = pointIndex + 3;
                v.left = pointIndex - 3;
                lineChartView.setCurrentViewport(v);
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    private void resetData() {
        Log.d(TAG, "resetData: ");
        record = LocalDateBaseHelper.getAllBloodPressureData(user_id);
        bloodPressureItemList = record.getBloodPressureItemListForChart();
        initChartData();
        initLineChart();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: isMultiChoose" + isMultiChoose);
        if (isMultiChoose) {
            isMultiChoose = false;
            for (ItemForAdapter i :
                    itemForAdapterList) {
                i.setMultiChoose(isMultiChoose);
                i.setChecked(false);
            }
            detailAdapter.notifyDataSetChanged();
            lvDetail.setLongClickable(true);
            fab.setImageResource(R.drawable.ic_add_white_24dp);
            return;
        }
        if (isDataChange) {
            Log.d(TAG, "onBackPressed: isDateChange" + isDataChange);
            setResult(RESULT_CODE_CHANGE_BPDA);
            finish();
            super.onBackPressed();
        } else {
            setResult(RESULT_CODE_NO_CHANGE_BPDA);
            finish();
            super.onBackPressed();
        }

    }

     class ItemForAdapter {
        private boolean isMultiChoose;
        private boolean isChecked;
        private BloodPressureItem item;

        public ItemForAdapter(boolean isMultiChoose, boolean isChecked, BloodPressureItem item) {
            this.isMultiChoose = isMultiChoose;
            this.isChecked = isChecked;
            this.item = item;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public BloodPressureItem getItem() {
            return item;
        }

        public void setItem(BloodPressureItem item) {
            this.item = item;
        }

        public boolean isMultiChoose() {
            return isMultiChoose;
        }

        public void setMultiChoose(boolean multiChoose) {
            isMultiChoose = multiChoose;
        }
    }
}

class MyDetailAdapter extends BaseAdapter {
    private static final String TAG = "TAG" + "MyDetailAdapter";
    private List<BloodPressureDetailActivity.ItemForAdapter> list;
    private LayoutInflater inflater;
    private OnDetailItemClickListener onDetailItemClickListener;
    private Context context;

    public MyDetailAdapter(Context context, List<BloodPressureDetailActivity.ItemForAdapter> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

    }

    public interface OnDetailItemClickListener {
        public void onDetailItemClick(int position, boolean isChecked);
    }

    public void setOnDetailItemClickListener(OnDetailItemClickListener l) {
        this.onDetailItemClickListener = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BloodPressureDetailActivity.ItemForAdapter item = getItem(position);
        View view;
        class ViewHolder {
            TextView txtItemDate;
            TextView txtItemHP;
            TextView txtItemLP;
            CheckBox checkBox;
        }

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.item_bp_one_day_detail, null);
            viewHolder.txtItemDate = (TextView) view.findViewById(R.id.txt_bp_detail_date);
            viewHolder.txtItemHP = (TextView) view.findViewById(R.id.txt_bp_detail_high);
            viewHolder.txtItemLP = (TextView) view.findViewById(R.id.txt_bp_detail_low);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_select);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        float high = item.getItem().getSystolicPressure();
        float low = item.getItem().getDiastolicPressure();
        viewHolder.txtItemDate.setText(HelpUtils.getTimeWithoutSecInString(item.getItem().getDate()));
        viewHolder.txtItemHP.setText(String.valueOf(high));
        viewHolder.txtItemLP.setText(String.valueOf(low));
        viewHolder.checkBox.setChecked(item.isChecked());
        if (item.isMultiChoose()) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(TAG, "onCheckedChanged: " + position + " " + isChecked);
                    onDetailItemClickListener.onDetailItemClick(position, isChecked);
                }
            });
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }

        if (high < 90) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(context, R.color.low));
        } else if (high < 140) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        } else if (high < 160) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(context, R.color.one));
        } else if (high < 180) {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(context, R.color.two));
        } else {
            viewHolder.txtItemHP.setBackgroundColor(ContextCompat.getColor(context, R.color.three));
        }

        if (low < 60) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(context, R.color.low));
        } else if (low < 90) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        } else if (low < 100) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(context, R.color.one));
        } else if (low < 110) {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(context, R.color.two));
        } else {
            viewHolder.txtItemLP.setBackgroundColor(ContextCompat.getColor(context, R.color.three));
        }

        return view;
    }

    @Override
    public BloodPressureDetailActivity.ItemForAdapter getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}



