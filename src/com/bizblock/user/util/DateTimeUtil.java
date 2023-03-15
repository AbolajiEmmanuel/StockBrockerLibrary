 
package com.bizblock.user.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Praise
 */
public class DateTimeUtil
{
    public static class TimeRange
    {
        private Timestamp beginTime;

        private Timestamp endTime;

        public TimeRange()
        {
        }

        public Timestamp getBeginTime()
        {
            return beginTime;
        }

        public void setBeginTime(Timestamp beginTime)
        {
            this.beginTime = beginTime;
        }

        public Timestamp getEndTime()
        {
            return endTime;
        }

        public void setEndTime(Timestamp endTime)
        {
            this.endTime = endTime;
        }
    }

    public static TimeRange getAnyTimeRange(Calendar beginCal, Calendar endCal)
    {
        Timestamp beginTS = new Timestamp(beginCal.getTime().getTime());
        Timestamp endTs = new Timestamp(endCal.getTime().getTime());
        TimeRange tr = new TimeRange();
        tr.setBeginTime(beginTS);
        tr.setEndTime(endTs);
        return tr;
    }

    public static TimeRange getDayTimeRange(Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(year, month, day, 0, 0, 0);
        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month, day, 23, 59, 59);
        return getAnyTimeRange(beginCal, endCal);
    }

    public static TimeRange getWeekTimeRange(Calendar cal)
    {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar beginCal = (Calendar)cal.clone();
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Calendar endCal = cal;
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        return getAnyTimeRange(beginCal, endCal);
    }

    public static TimeRange getMonthTimeRange(Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar beginCal = new GregorianCalendar(year, month, 1, 0, 0, 0);
        Calendar endCal = new GregorianCalendar(year, month, daysInMonth, 23, 59, 59);
        return getAnyTimeRange(beginCal, endCal);
    }

    public static TimeRange getYearTimeRange(Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        Calendar beginCal = new GregorianCalendar(year, 0, 1, 0, 0, 0);
        Calendar endCal = new GregorianCalendar(year, 11, 31, 23, 59, 59);
        return getAnyTimeRange(beginCal, endCal);
    }

    public static TimeRange getTrialExpiryDate()
    {
        Calendar cal = new GregorianCalendar();
        Timestamp beginTS = new Timestamp(cal.getTime().getTime());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Timestamp endTs = new Timestamp(cal.getTime().getTime());
        TimeRange tr = new TimeRange();
        tr.setBeginTime(beginTS);
        tr.setEndTime(endTs);
        return tr;
    }

    public static Timestamp getTodayTimeZone()
    {
        GregorianCalendar todayCal = new GregorianCalendar();
        todayCal.add(Calendar.HOUR_OF_DAY, 6);
        return new Timestamp(todayCal.getTime().getTime());
    }

    public static Timestamp getWCATimeZone(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 6);
        return new Timestamp(cal.getTime().getTime());
    }

    public static String getCurrFormattedDate()
    {
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        return df.format(now);
    }

    public static String getCurrFormattedTime()
    {
        Date now = new Date();
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
        return tf.format(now);
    }

    public static String getFormattedDate(Date date)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        return df.format(date);
    }

    public static String getFormattedTime(Date date)
    {
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);
        return tf.format(date);
    }

    public static boolean isSameDate(Calendar cal1, Calendar cal2)
    {
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
