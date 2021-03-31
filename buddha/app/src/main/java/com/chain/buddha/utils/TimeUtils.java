package com.chain.buddha.utils;

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
}
