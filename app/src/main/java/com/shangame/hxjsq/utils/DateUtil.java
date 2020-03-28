package com.shangame.hxjsq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public class DateUtil {

    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timber.d("前7天==" + dft.format(endDate));
        return dft.format(endDate);
    }

    public static String formatData(long timeStamp){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeStamp);
        res = simpleDateFormat.format(date);
        return res;
    }
}
