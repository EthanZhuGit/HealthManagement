package com.example.healthmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.healthmanagement.model.BloodPressureItem;
import com.example.healthmanagement.model.BloodSugarItem;
import com.example.healthmanagement.model.IsCardShow;
import com.example.healthmanagement.model.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class HelpUtils {
    private static final String TAG = "TAG" + "HelpUtils";
    private static final String BLOODPRESSUREKEY = "blood_pressure_is_show";
    private static final String BLOODSUGARKEY = "blood_sugar_is_show";
    private static final String STEPCOUNTKEY = "step_count_is_show";
    private static final String HEARTRATEKEY = "heart_rate_is_show";
    private static final String BLOODOXYGENKEY = "blood_oxygen_is_show";
    private static final String KEY = "is_show";

//    private static final String[] array = new String[]{Record.STEP_COUNT, Record.BLOOD_PRESSURE, Record.BLOOD_SUGAR, Record.HEART_RATE, Record.BLOOD_OXYGEN};


    public static ArrayList<IsCardShow> getCardShowControlList(Context context) {
        String[] array = new String[]{Record.BLOOD_PRESSURE, Record.BLOOD_SUGAR, Record.HEART_RATE, Record.BLOOD_OXYGEN};

        ArrayList<IsCardShow> isCardShows = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        for (int i = 0; i < array.length; i++) {
            IsCardShow it = new IsCardShow("", false);
            isCardShows.add(it);
        }
        for (int i = 0; i < array.length; i++) {
            isCardShows.set(preferences.getInt(array[i], i),
                    new IsCardShow(array[i], preferences.getBoolean(array[i] + KEY, true)));
        }
//        isCardShows.set(preferences.getInt(Record.STEP_COUNT, 0),
//                new IsCardShow(Record.STEP_COUNT, preferences.getBoolean(STEPCOUNTKEY, true)));
//        isCardShows.set(preferences.getInt(Record.BLOOD_PRESSURE, 1),
//                new IsCardShow(Record.BLOOD_PRESSURE, preferences.getBoolean(BLOODPRESSUREKEY, true)));
//        isCardShows.set(preferences.getInt(Record.BLOOD_SUGAR, 2),
//                new IsCardShow(Record.BLOOD_SUGAR, preferences.getBoolean(BLOODSUGARKEY, true)));
//        isCardShows.set(preferences.getInt(Record.HEART_RATE, 3),
//                new IsCardShow(Record.HEART_RATE, preferences.getBoolean(HEARTRATEKEY, true)));
//        isCardShows.set(preferences.getInt(Record.BLOOD_OXYGEN, 4),
//                new IsCardShow(Record.BLOOD_OXYGEN, preferences.getBoolean(BLOODOXYGENKEY, true)));
        Log.d(TAG, "getCardShowControlList: ");
        return isCardShows;
    }

    public static void saveCardShowStatus(Context context, ArrayList<IsCardShow> isCardShowList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < isCardShowList.size(); i++) {
            IsCardShow isCardShow = isCardShowList.get(i);
            editor.putInt(isCardShow.getName(), i);
            editor.putBoolean(isCardShow.getName() + KEY, isCardShow.isShow());
        }
        editor.apply();
    }

    public static String getTimeWithoutSecInString(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static Date getDateWithoutMillionSec(Date date) {
        Date output=null;
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            output = out.parse(in.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (output == null) {
            return null;
        }
        return output;
    }

    public static String getMonthDayInString(Date date) {
        SimpleDateFormat in = new SimpleDateFormat("M/d");
        return in.format(date);
    }

}
