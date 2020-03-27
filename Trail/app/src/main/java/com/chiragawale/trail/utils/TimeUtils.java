package com.chiragawale.trail.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static String currentTimeStamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE dd, MMMM yyyy, hh:mm aa");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }


}