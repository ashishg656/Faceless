package com.example.faceless.extras;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by praveen goel on 10/14/2015.
 */
public class TimeUtils {

    public static String getTimeForChats(String timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            Date date = simpleDateFormat.parse(timestamp);

            return getActivityDate(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getActivityDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        SpannableStringBuilder builder1 = new SpannableStringBuilder();

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dayName = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        builder1.append(dayName + ", ");

        String date = getMonth(calendar.get(Calendar.MONTH)) + " "
                + String.format("%02d", day);

        SpannableString dateSpannable = new SpannableString(date);
        dateSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                date.length(), 0);
        builder1.append(dateSpannable);

        String year = " " + calendar.get(Calendar.YEAR);
        SpannableString yearSpannable = new SpannableString(year);
        yearSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                year.length(), 0);
        yearSpannable.setSpan(new RelativeSizeSpan(1.5f), 0, year.length(), 0);
        builder1.append(yearSpannable);

        return builder1.toString();
    }

    private static String getDayOfWeek(int dayOfWeek) {
        String result = "";
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                result = "Mon";
                break;
            case Calendar.TUESDAY:
                result = "Tue";
                break;
            case Calendar.WEDNESDAY:
                result = "Wed";
                break;
            case Calendar.THURSDAY:
                result = "Thu";
                break;
            case Calendar.FRIDAY:
                result = "Fri";
                break;
            case Calendar.SATURDAY:
                result = "Sat";
                break;
            case Calendar.SUNDAY:
                result = "Sun";
                break;
        }
        return result;
    }

    public static String getMonth(int month) {

        String result = "";
        switch (month) {

            case Calendar.JANUARY:
                result = "Jan";
                break;

            case Calendar.FEBRUARY:
                result = "Feb";
                break;

            case Calendar.MARCH:
                result = "Mar";
                break;

            case Calendar.APRIL:
                result = "Apr";
                break;

            case Calendar.MAY:
                result = "May";
                break;

            case Calendar.JUNE:
                result = "June";
                break;

            case Calendar.JULY:
                result = "July";
                break;

            case Calendar.AUGUST:
                result = "Aug";
                break;

            case Calendar.SEPTEMBER:
                result = "Sep";
                break;

            case Calendar.OCTOBER:
                result = "Oct";
                break;

            case Calendar.NOVEMBER:
                result = "Nov";
                break;

            case Calendar.DECEMBER:
                result = "Dec";
                break;
        }
        return result;
    }
}
