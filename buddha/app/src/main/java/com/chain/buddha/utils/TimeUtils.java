package com.chain.buddha.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String timeStamp2Date(String timeString) {
        try {
            long time = Long.parseLong(timeString);
            String format = "MM-dd HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            return "";
        }
    }

    public static long getStringToDate(String dateString) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }

    }
}
