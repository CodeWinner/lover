package com.bacnk.lovedays.common;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.bacnk.lovedays.common.exception.LoveDaysCountDayException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bacnk.lovedays.common.LoveCommon.BACH_DUONG;
import static com.bacnk.lovedays.common.LoveCommon.BAO_BINH;
import static com.bacnk.lovedays.common.LoveCommon.BO_CAP;
import static com.bacnk.lovedays.common.LoveCommon.CU_GIAI;
import static com.bacnk.lovedays.common.LoveCommon.KIM_NGUU;
import static com.bacnk.lovedays.common.LoveCommon.MA_KET;
import static com.bacnk.lovedays.common.LoveCommon.NHAN_MA;
import static com.bacnk.lovedays.common.LoveCommon.PATH_PATERM;
import static com.bacnk.lovedays.common.LoveCommon.SONG_NGU;
import static com.bacnk.lovedays.common.LoveCommon.SONG_TU;
import static com.bacnk.lovedays.common.LoveCommon.SU_TU;
import static com.bacnk.lovedays.common.LoveCommon.THIEN_BINH;
import static com.bacnk.lovedays.common.LoveCommon.XU_NU;

/**
 *
 * @author code4lifevn
 */
public class DateUtils {

    private static Pattern dateRegexPattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
    private static  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static boolean isValidDate(String dateString) {
        Matcher dateMatcher = dateRegexPattern.matcher(dateString);

        if (dateMatcher.matches()) {

            dateMatcher.reset();

            if (dateMatcher.find()) {
                String day = dateMatcher.group(1);
                String month = dateMatcher.group(2);
                int year = Integer.parseInt(dateMatcher.group(3));

                if ("31".equals(day) &&
                        ("4".equals(month) || "6".equals(month) || "9".equals(month) ||
                                "11".equals(month) || "04".equals(month) || "06".equals(month) ||
                                "09".equals(month))) {
                    return false; // 1, 3, 5, 7, 8, 10, 12 has 31 days
                } else if ("2".equals(month) || "02".equals(month)) {
                    //leap year
                    if (year % 4 == 0) {
                        return !"30".equals(day) && !"31".equals(day);
                    } else {
                        return !"29".equals(day) && !"30".equals(day) && !"31".equals(day);
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Count day
     * @param startDay
     * @return
     */
    public static long getNumDaysFromCurrentTime(String startDay) throws LoveDaysCountDayException {

        if (startDay == null) {
            throw new LoveDaysCountDayException();
        }

        try {
            Date startDate = sdf.parse(startDay);
            Date currentDate = Calendar.getInstance().getTime();

            // Check equals
            if (startDate.equals(currentDate)) {
                return 1;
            }

            if (startDate.after(currentDate)) {
                return 0;
            }

            long diff = currentDate.getTime() - startDate.getTime();

            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
        } catch (ParseException e) {
            throw new LoveDaysCountDayException();
        }
    }

    /**
     * Count day
     * @param startDay
     * @return
     */
    public static String addDate(String startDay ,int numberOfDays) throws LoveDaysCountDayException {

        if (startDay == null) {
            throw new LoveDaysCountDayException();
        }

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(startDay));
            c.add(Calendar.DAY_OF_YEAR, numberOfDays);
        } catch (ParseException e) {
            throw new LoveDaysCountDayException();
        }
        return sdf.format(c.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DateUnitCommon getTimeBetween(String startDay) throws LoveDaysCountDayException {
        if (startDay == null) {
            throw new LoveDaysCountDayException();
        }

        DateTimeFormatter f = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            f = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Resources.getSystem().getConfiguration().getLocales().get(0));

        }
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String todate = sdf.format(today);

        LocalDate endDate = LocalDate.parse(todate, f);;
        LocalDate startDate = LocalDate.parse(startDay, f);
        Period period = Period.between(startDate, endDate);

        DateUnitCommon dateUnitCommon = new DateUnitCommon();
        dateUnitCommon.setYears(period.getYears());
        dateUnitCommon.setMonths(period.getMonths());

        int weeks = period.getDays()/7;
        dateUnitCommon.setWeeks(weeks);
        int days = period.getDays()%7;
        dateUnitCommon.setDays(days);

        int hours = today.getHours();
        dateUnitCommon.setHours(hours);

        int minute = today.getMinutes();
        dateUnitCommon.setMinutes(minute);

        int second = today.getSeconds();
        dateUnitCommon.setSeconds(second);

        dateUnitCommon.setStrDay(startDay);
        return dateUnitCommon ;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getOld(String birthDay) throws LoveDaysCountDayException {
        if (birthDay == null) {
            throw new LoveDaysCountDayException();
        }
        DateTimeFormatter f = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            f = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Resources.getSystem().getConfiguration().getLocales().get(0));

        }
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String todate = sdf.format(today);

        LocalDate endDate = LocalDate.parse(todate, f);;
        LocalDate startDate = LocalDate.parse(birthDay, f);
        Period period = Period.between(startDate, endDate);

        return period.getYears();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getCHD(String birthDay) throws LoveDaysCountDayException {
        if (birthDay == null) {
            throw new LoveDaysCountDayException();
        }
        DateTimeFormatter f = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            f = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Resources.getSystem().getConfiguration().getLocales().get(0));

        }
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        LocalDate startDate = LocalDate.parse(birthDay, f);

        String yearForBirthDay = birthDay.split(PATH_PATERM)[2];

        // Bach duong
        if (startDate.isAfter(LocalDate.parse("20/03" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("21/04" + PATH_PATERM + yearForBirthDay, f))) {
            return BACH_DUONG;
        } else if (startDate.isAfter(LocalDate.parse("20/04" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("21/05" + PATH_PATERM + yearForBirthDay, f))) {
            return KIM_NGUU;
        } else if (startDate.isAfter(LocalDate.parse("20/05" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("22/06" + PATH_PATERM + yearForBirthDay, f))) {
            return SONG_TU;
        } else if (startDate.isAfter(LocalDate.parse("21/06" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("23/07" + PATH_PATERM + yearForBirthDay, f))) {
            return CU_GIAI;
        } else if (startDate.isAfter(LocalDate.parse("22/07" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("23/08" + PATH_PATERM + yearForBirthDay, f))) {
            return SU_TU;
        } else if (startDate.isAfter(LocalDate.parse("22/08" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("23/09" + PATH_PATERM + yearForBirthDay, f))) {
            return XU_NU;
        } else if (startDate.isAfter(LocalDate.parse("22/09" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("24/10" + PATH_PATERM + yearForBirthDay, f))) {
            return THIEN_BINH;
        } else if (startDate.isAfter(LocalDate.parse("23/10" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("23/11" + PATH_PATERM + yearForBirthDay, f))) {
            return BO_CAP;
        } else if (startDate.isAfter(LocalDate.parse("22/11" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("22/12" + PATH_PATERM + yearForBirthDay, f))) {
            return NHAN_MA;
        } else if (startDate.isAfter(LocalDate.parse("21/12" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("20/01" + PATH_PATERM + yearForBirthDay, f))) {
            return MA_KET;
        } else if (startDate.isAfter(LocalDate.parse("19/01" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("19/02" + PATH_PATERM + yearForBirthDay, f))) {
            return BAO_BINH;
        } else if (startDate.isAfter(LocalDate.parse("18/02" + PATH_PATERM + yearForBirthDay, f))
                && startDate.isBefore(LocalDate.parse("21/03" + PATH_PATERM + yearForBirthDay, f))) {
            return SONG_NGU;
        }

        return -1;
    }
}