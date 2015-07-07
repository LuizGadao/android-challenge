package br.com.luizcarlos.testinstaflickr.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by luizcarlos on 06/07/15.
 */
public class TimeUtils {

    public static String timePassed(Date date){
        long diffInMillisec = Calendar.getInstance().getTimeInMillis() - date.getTime();
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds( diffInMillisec );
        long seconds = diffInSec % 60;
        diffInSec/= 60;
        long minutes =diffInSec % 60;
        diffInSec /= 60;
        long hours = diffInSec % 24;
        diffInSec /= 24;
        long days = diffInSec;

        String str = "";
        if ( days != 0 ) str = days + "d";
        else if ( hours != 0 ) str = hours + "h";
        else if ( minutes != 0 ) str = minutes + "m";
        else if ( seconds != 0 ) str = seconds + "s";

        return str;
    }

}
