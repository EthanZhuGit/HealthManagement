package com.example.healthmanagement.datebase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zyx10 on 2017/5/13 0013.
 */

public class LocalDateBaseHelper {




    /**
     * 获取七天之前的开始时间
     * 今天是5月13号任何时间，则返回5月7号 00:00:00
     * @return
     */
    public static Date getSenvenDaysBeforStartTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)-6,
                0, 0, 0);
        return calendar1.getTime();
    }
}
