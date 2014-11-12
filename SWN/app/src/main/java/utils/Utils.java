package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kumudini on 9/23/14.
 */
public class Utils {


    private static  final String LOG_TAG =  Utils.class.getSimpleName();

    public static String getFormattedDateString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return simpleDateFormat.format(date);
    }
}
