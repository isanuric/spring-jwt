package com.isanuric.bar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
 * ----------------------------------------------
 * (c) 2018 Copyright iC Consult GmbH
 * <p/>
 * Project: bar
 * @author ehsan.salmani@ic-consult.de on 17/11/2018.
 */
public class Utils {


    public static String getCurrentTimeStamp() {

        Date currentDate = new Date();
        SimpleDateFormat PingIDDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PingIDDateFormat.setTimeZone(TimeZone.getTimeZone("Berlin"));

        return PingIDDateFormat.format(currentDate);
    }

}
