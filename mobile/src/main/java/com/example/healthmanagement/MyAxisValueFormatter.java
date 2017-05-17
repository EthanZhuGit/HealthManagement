package com.example.healthmanagement;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zyx10 on 2017/5/16 0016.
 */

public class MyAxisValueFormatter implements IAxisValueFormatter {
    private static final String TAG = "TAG" + "MyAxisValueFormatter";
    private ArrayList<String> values;
    public MyAxisValueFormatter(ArrayList<String> values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.d(TAG, "getFormattedValue: " + value+" "+(int)value);
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
