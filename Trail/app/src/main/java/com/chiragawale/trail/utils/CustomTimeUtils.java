package com.chiragawale.trail.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomTimeUtils {

    public static String currentTimeStamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE dd, MMMM yyyy, hh:mm aa");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    public static long currentTimestampLong() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public static long trimmedCurrentTimestampLong() {
        Calendar c = Calendar.getInstance();
        return trimDate(c.getTimeInMillis());
    }

    public static long trimDate(long date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = dateformat.format(date);
        long trimmedDate  = 0;
        try {
            trimmedDate = dateformat.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trimmedDate;
    }


}