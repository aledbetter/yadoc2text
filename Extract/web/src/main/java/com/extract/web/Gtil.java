package com.extract.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;


/*
 * Service util class to contain generic Service functionality for use in all service objects.
 * get and del are both implemented here 
 */
public class Gtil {
	static public String no_space [] = {",", ":", ";", ".", "!", "?"};

    // the desired format
    public static String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String display_pattern_sec = "EEE MMM d h:mm:ss a z yyyy";
    public static String display_pattern_msec = "EEE MMM d h:mm:ss.SSS a z yyyy";
    public static String display_pattern = "EEE MMM d h:mm a z yyyy";
    public static String email_pattern = "yyyy-MM-dd HH:mm:ss";
    public static String display_pattern_clock = "h:mm a";
    public static String display_pattern_24clock = "HH:mm|EEE";
    public static String date_pattern = "M/d/yy";
    public static String date_pattern1 = "M/d/yyyy";
    public static String date_pattern2 = "EEEEEEEEEEEE, MMMMMMMMMMMMMMM d";
    public static String date_pattern3 = "MMMMMMMMMMMMMMM d, yyyy";
    public static String dob_pattern = "yyyy-MM";
    public static String dob_pattern2 = "yyyy-MM-dd";
    public static String dob_pattern4 = "MM/dd/yyyy";
    public static String dob_pattern10 = "yyyyMMdd";
    public static String dob_pattern5 = "yyyy";
    public static String dob_pattern8 = "w"; // week in year
    public static String dob_pattern9 = "D"; // day in year
    public static String date_pattern_Month = "MMMMMMMMMMMMMMM";
    public static String date_pattern_Month3 = "MMM";

    public static String ont_time_pattern = "M-d-yyyy-HH:mm:ss.SSS";
    public static String ont_date_pattern = "M-d-yyyy";
    public static String ont_week_pattern = "'w'w-yyyy";
    public static String ont_Month_pattern = "'m'M-yyyy";
    public static String ont_year_pattern = "'y'yyyy";

	@SuppressWarnings("unused")

    // Get a new GUID (not inteded for printing)
	public static BigInteger getGUID() {
		UUID uu = UUID.randomUUID();
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uu.getMostSignificantBits());
		bb.putLong(uu.getLeastSignificantBits());
		return new BigInteger(1,bb.array());
	}


	// get the current time as a string
	public static String getCurrentTimeStamp() {
		return formatTimeStamp(Gtil.getUTCTime());
	}
	public static String getCurrentDate() {
		return formatDateString(Gtil.getUTCTime());
	}
	public static int getBankSchedHour(String time) {
		int i = time.indexOf(":");
		return Gtil.toInt(time.substring(0, i));
	}
	public static int getBankSchedMin(String time) {
		int i = time.indexOf(":");
		int e = time.indexOf("|");
		return Gtil.toInt(time.substring((i+1), e));
	}
	public static String getBankSchedType(String time) {
		int e = time.indexOf("|");
		return time.substring((e+1), time.length());
	}
	public static int getBankSchedAltDay(String time) {
		int e = time.indexOf("|");
		String d = time.substring((e+1), time.length());
		if (d.equalsIgnoreCase("sun")) return Calendar.SUNDAY;
		else if (d.equalsIgnoreCase("mon")) return Calendar.MONDAY;
		else if (d.equalsIgnoreCase("tue")) return Calendar.TUESDAY;
		else if (d.equalsIgnoreCase("wed")) return Calendar.WEDNESDAY;
		else if (d.equalsIgnoreCase("thu")) return Calendar.THURSDAY;
		else if (d.equalsIgnoreCase("fri")) return Calendar.FRIDAY;
		else if (d.equalsIgnoreCase("sat")) return Calendar.SATURDAY;
		return 0;
	}
	public static int getCalendarDay(String day) {
		if (day == null) return -1;
		if (day.equalsIgnoreCase("sunday")) return Calendar.SUNDAY;
		else if (day.equalsIgnoreCase("monday")) return Calendar.MONDAY;
		else if (day.equalsIgnoreCase("tuesday")) return Calendar.TUESDAY;
		else if (day.equalsIgnoreCase("wednesday")) return Calendar.WEDNESDAY;
		else if (day.equalsIgnoreCase("thursday")) return Calendar.THURSDAY;
		else if (day.equalsIgnoreCase("friday")) return Calendar.FRIDAY;
		else if (day.equalsIgnoreCase("saturday")) return Calendar.SATURDAY;
		return -1;
	}
	public static int getPstHour(Calendar time) {
		String pstTime = Gtil.formatTimeString24Clock(time, "PST");
		return getBankSchedHour(pstTime);
	}
	public static int getPstHourOffset(Calendar time) {
/*		int gmtH = ServiceUtil.getGMTHour(time);
		int pstH = ServiceUtil.getPstHour(time);
	    System.out.println("    getPstHourOffset["+pstH+"]: "+ServiceUtil.formatTimeString24Clock(start, "PST"));

		int off = (gmtH-pstH);
		if (off < 0) return -((gmtH+24)-pstH);
		return off;*/
		return Gtil.getPstHour(time);
	}

	public static int getGMTHour(Calendar time) {
		String pstTime = Gtil.formatTimeString24Clock(time);
		return getBankSchedHour(pstTime);
	}
	public static int getPstHour() {
		return getPstHour(Gtil.getUTCTime());
	}
	public static int getPstMinute(Calendar time) {
		String pstTime = Gtil.formatTimeString24Clock(time, "PST");
		return getBankSchedMin(pstTime);
	}
	public static int getPstMinute() {
		return getPstMinute(Gtil.getUTCTime());
	}
	public static int getPstDayOfWeek(Calendar time) {
		String pstTime = Gtil.formatTimeString24Clock(time, "PST");
		return getBankSchedAltDay(pstTime);
	}
	public static int getPstDayOfWeek() {
		return getPstDayOfWeek(Gtil.getUTCTime());
	}
	public static int getPstDayOfMonth(Calendar time) {
		String pstTime = Gtil.formatDob2String(time, "PST");
		pstTime = pstTime.substring(8, pstTime.length()); //"yyyy-MM-dd"
		return toInt(pstTime);
	}
	public static int getPstDayOfMonth() {
		return getPstDayOfMonth(Gtil.getUTCTime());
	}
	public static int getPstDayOfYear(Calendar time) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern9);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("PST")));
		return toInt(simpleDateFormat.format(time.getTime()));
	}
	public static int getPstDayOfYear() {
		return getPstDayOfYear(Gtil.getUTCTime());
	}
	public static int getPstWeekOfYear(Calendar time) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern8);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("PST")));
		return toInt(simpleDateFormat.format(time.getTime()));
	}
	public static int getPstWeekOfYear() {
		return getPstWeekOfYear(Gtil.getUTCTime());
	}
	public static int getPstMonth(Calendar time) {
		String pstTime = Gtil.formatDob2String(time, "PST");
		pstTime = pstTime.substring(5, 7); //"yyyy-MM-dd"
		return toInt(pstTime);
	}
	public static int getPstMonth() {
		return getPstMonth(Gtil.getUTCTime());
	}
	public static int getPstYear(Calendar time) {
		String pstTime = Gtil.formatDob2String(time, "PST");
		pstTime = pstTime.substring(0, 4); //"yyyy-MM-dd"
		return toInt(pstTime);
	}
	public static int getPstYear() {
		return getPstYear(Gtil.getUTCTime());
	}
	// format the time we want
	public static String formatTimeStamp(Calendar time) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatTimeStamp(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatTimeStampNoGMT(Calendar time) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	return simpleDateFormat.format(time.getTime());
	}
	// format the time for display in templates and such
	public static String formatDateString(Calendar time) {
		return formatDateString(time, "GMT");
	}
	public static String formatDateString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatDateFullYear(Calendar time) {
		return formatDateFullYear(time, "GMT");
	}
	public static String formatDateFullYear(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern1);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatDateDayString(Calendar time) {
		return formatDateDayString(time, "GMT");
	}
	public static String formatDateDayString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern2);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatDateLongString(Calendar time) {
		return formatDateLongString(time, "GMT");
	}
	public static String formatDateLongString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern3);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatDatefullYearString(Calendar time) {
		return formatDatefullYearString(time, "GMT");
	}
	public static String formatDatefullYearString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern4);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatYearString(Calendar time) {
		return formatYearString(time, "GMT");
	}
	public static String formatYearString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern5);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}

	public static String formatMonth(Calendar time) {
		return formatMonth(time, "GMT");
	}
	public static String formatMonth(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern_Month);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatMonth3(Calendar time) {
		return formatMonth3(time, "GMT");
	}
	public static String formatMonth3(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_pattern_Month3);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}


	// format the time for display in templates and such
	public static String formatDobString(Calendar time) {
		return formatDobString(time, "GMT");
	}
	public static String formatDobString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}

	public static String formatDob2String(Calendar time) {
		return formatDob2String(time, "GMT");
	}
	public static String formatDob2String(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dob_pattern2);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
    public static Calendar loadDob2(String date_string) {
    	if (date_string == null || date_string.equalsIgnoreCase("(null)") || date_string.equalsIgnoreCase("[?]")) {
    		return null;
    	}
    	// hack, we may have the '+' converted to a ' ' by the html gunk
    	date_string.trim();
    	date_string = date_string.replace(' ', '+');
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string);
	    	Calendar date = dateFormat.getCalendar();
	    	date.set(Calendar.HOUR_OF_DAY, 11);
	    	return date;
    	} catch (Throwable t) {
    		return null;
    	}
    }

    public static Calendar makeDate(Calendar time) {
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
    }
    // ont range
	public static String fmtRangeName(Calendar start, Calendar end, String label) {
		if (label != null) return Gtil.fmtOntTime(start) + "_" + Gtil.fmtOntTime(end) + "_" + label;
		return Gtil.fmtOntTime(start) + "_" + Gtil.fmtOntTime(end);
	}
	public static Calendar loadRangeStart(String range) {
		if (range == null) return null;
		int idx = range.indexOf("_");
		if (idx < 0) return null;
		return Gtil.loadOntTime(range.substring(0, idx));
	}
	public static Calendar loadRangeEnd(String range) {
		if (range == null) return null;
		int idx = range.indexOf("_");
		if (idx < 0) return null;
		String r = range.substring(idx+1, range.length());
		idx = r.indexOf("_");
		if (idx > 0) r = range.substring(0, idx);
		return Gtil.loadOntTime(r);
	}
	public static String loadRangeLabel(String range) {
		if (range == null) return null;
		int idx = range.indexOf("_");
		if (idx < 0) return null;
		String r = range.substring(idx+1, range.length());
		idx = r.indexOf("_");
		if (idx <= 0) return null;
		return r.substring(idx+1, r.length());
	}


    // ont time
	public static String fmtOntTime(Calendar time) {
		if (time == null) return null;
		int h = time.get(Calendar.HOUR_OF_DAY);
		int m = time.get(Calendar.MINUTE);
		int s = time.get(Calendar.SECOND);
		int ms = time.get(Calendar.MILLISECOND);
		if (h == 0 && m == 0 && s == 0 && ms == 0) {
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_date_pattern);
	    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
	    	return simpleDateFormat.format(time.getTime());
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_time_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String fmtOntDate(Calendar time) {
		if (time == null) return null;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_date_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String fmtOntWeek(Calendar time) {
		if (time == null) return null;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_week_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
    	return simpleDateFormat.format(time.getTime());
	}
	public static Calendar loadOntWeek(String date_string) {
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(ont_week_pattern);
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string.trim());
	    	return dateFormat.getCalendar();
    	} catch (Throwable t) {
    		return null;
    	}
	}
	public static String fmtOntMonth(Calendar time) {
		if (time == null) return null;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_Month_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
    	return simpleDateFormat.format(time.getTime());
	}
	public static Calendar loadOntMonth(String date_string) {
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(ont_Month_pattern);
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string.trim());
	    	return dateFormat.getCalendar();
    	} catch (Throwable t) {
    		return null;
    	}
	}
	public static String fmtOntYear(Calendar time) {
		if (time == null) return null;
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ont_year_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr("GMT")));
    	return simpleDateFormat.format(time.getTime());
	}
	public static Calendar loadOntYear(String date_string) {
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(ont_year_pattern);
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string.trim());
	    	return dateFormat.getCalendar();
    	} catch (Throwable t) {
    		return null;
    	}
	}

    public static Calendar loadOntTime(String date_string) {
    	try {
    		if (date_string.contains("_")) return null; // is range
    		if (date_string.contains(":")) {
    	    	SimpleDateFormat dateFormat = new SimpleDateFormat(ont_time_pattern);
    	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    	    	dateFormat.parse(date_string.trim());
    	    	return dateFormat.getCalendar();
    		} else if (date_string.startsWith("y")) {
    			return null; // not yet
    		} else if (date_string.startsWith("m")) {
    			return null; // not yet
    		} else if (date_string.startsWith("w")) {
    			return null; // not yet
    		} else {
    	    	SimpleDateFormat dateFormat = new SimpleDateFormat(ont_date_pattern);
    	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    	    	dateFormat.parse(date_string.trim());
    	    	return dateFormat.getCalendar();
    		}
    	} catch (Throwable t) {
    		return null;
    	}
    }



	// format the time for display in templates and such
	public static String formatTimeString(Calendar time) {
    	return formatTimeString(time, "GMT");
	}
	// format the time for display in templates and such
	public static String formatTimeString(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(display_pattern);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	// display with seconds
	public static String formatTimeStringSec(Calendar time) {
    	return formatTimeStringSec(time, "GMT");
	}
	public static String formatTimeStringSec(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(display_pattern_sec);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}

	// display with milliseconds
	public static String formatTimeStringMSec(Calendar time) {
    	return formatTimeStringMSec(time, "GMT");
	}
	public static String formatTimeStringMSec(Calendar time, String time_zone) {
		if (time == null) {
			return null;
		}
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(display_pattern_msec);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
    public static Calendar loadTimeStamp(String timeStampString) {
    	if (timeStampString == null || timeStampString.equalsIgnoreCase("(null)") || timeStampString.equalsIgnoreCase("[?]")) {
    		return null;
    	}
    	// hack, we may have the '+' converted to a ' ' by the html gunk
    	timeStampString.trim();
    	timeStampString = timeStampString.replace(' ', '+');

    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(timeStampString);
	    	Calendar cal = dateFormat.getCalendar();
	    	//cal = ServiceUtil.convertToGmt(cal);
	    	return cal;
    	} catch (Throwable t) {
    		//System.out.println("loadTimeStamp: ERROR: " + timeStampString);
    		return null;
    	}
    }
    public static Calendar loadEmailTimeStamp(String timeStampString) {
    	if (timeStampString == null) return null;
    	timeStampString.trim();
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat(email_pattern);
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(timeStampString);
	    	Calendar cal = dateFormat.getCalendar();
	    	return cal;
    	} catch (Throwable t) {
    		return null;
    	}
    }


	public static String formatTimeStringClock(Calendar time) {
    	return formatTimeStringClock(time, "GMT");
	}
	public static String formatTimeStringClock(Calendar time, String time_zone) {
		if (time == null) return null;

    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(display_pattern_clock);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}
	public static String formatTimeString24Clock(Calendar time) {
    	return formatTimeString24Clock(time, "GMT");
	}
	public static String formatTimeString24Clock(Calendar time, String time_zone) {
		if (time == null) return null;

    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(display_pattern_24clock);
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone(getTimeZoneStr(time_zone)));
    	return simpleDateFormat.format(time.getTime());
	}



    public static Calendar loadDate(String date_string) {
    	if (date_string == null || date_string.equalsIgnoreCase("(null)") || date_string.equalsIgnoreCase("[?]")) {
    		return null;
    	}
    	// hack, we may have the '+' converted to a ' ' by the html gunk
    	date_string.trim();
    	date_string = date_string.replace(' ', '+');
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy");
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string);
	    	Calendar date = dateFormat.getCalendar();
	    	date.set(Calendar.HOUR_OF_DAY, 11);
	    	return date;
    	} catch (Throwable t) {
    		return null;
    	}
    }

    public static Calendar loadDateFullYear(String date_string) {
    	if (date_string == null || date_string.equalsIgnoreCase("(null)") || date_string.equalsIgnoreCase("[?]")) {
    		return null;
    	}
    	// hack, we may have the '+' converted to a ' ' by the html gunk
    	date_string.trim();
    	date_string = date_string.replace(' ', '+');
    	try {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
	    	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	dateFormat.parse(date_string);
	    	Calendar date = dateFormat.getCalendar();
	    	date.set(Calendar.HOUR_OF_DAY, 11);
	    	return date;
    	} catch (Throwable t) {
    		return null;
    	}
    }

     public static Calendar setToTimeZone(Calendar time, String tz) {
		TimeZone z = TimeZone.getTimeZone(getTimeZoneStr(tz));
		time.setTimeZone(z); // set today to corect PST time
		long currentTime = time.getTimeInMillis();
		long convertedTime = currentTime + z.getOffset(currentTime);
		time.setTimeInMillis(convertedTime);
		return time;
    }

    public static Calendar setCalendarNumberDay(Calendar date, int instance, int day) {
    	if (date == null) return null;
    	date.set(Calendar.DAY_OF_MONTH, 1);
    	int nday = date.get(Calendar.DAY_OF_WEEK);

    	// days to the first instance
    	int cdays =  nday - day;
    	if (nday > day) {
    		cdays = 7 - (nday - day);
    	} else if (nday == day) {
    		cdays = 0;
    	} else if (nday < day) {
    		cdays = day - nday;
    	}
    	cdays++; // DAY_OF_WEEK has different base

    	cdays += (instance-1) * 7; // add the weeks
    	date.set(Calendar.DAY_OF_MONTH, cdays);
    	return date;
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
      //  Random rand;
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

	private static final int TT_MAX_ID = 200;
	private static long trac_time[] = null;
	private static long trac_time_cnt[] = null;
	private static long trac_time_total[] = null;

	public static void tracTimeStart(int id) {
		if (trac_time == null) tracTimeReset();
		trac_time[id] = System.currentTimeMillis();
	}
	public static void tracTimeSwtich(int last_id, int id) {
		trac_time[id] = tracTimeEnd(last_id);
	}
	public static long tracTimeEnd(int id) {
		long tt = System.currentTimeMillis();
		trac_time_cnt[id]++;
		trac_time_total[id] += (tt - trac_time[id]);
		trac_time[id] = 0;
		return tt;
	}
	public static void tracTimeShow(int id) {
		if (trac_time_cnt == null || trac_time_cnt[id] <= 0) return;
		long avg = trac_time_total[id] / trac_time_cnt[id];
		long sec = avg / 1000;
		long ms = avg - (sec * 1000);
		System.out.println(" TRAC["+id+"]  avg: " + sec + " sec "+ms+" ms   count: " + trac_time_cnt[id]);
	}
	public static void tracTimeShow() {
		for (int i=0;i<TT_MAX_ID;i++) tracTimeShow(i);
	}
	public static void tracTimeReset() {
		for (int i=0;i<TT_MAX_ID;i++) {
			trac_time = new long[TT_MAX_ID];
			trac_time_cnt = new long[TT_MAX_ID];
			trac_time_total = new long[TT_MAX_ID];
		}
	}

    /*
     * Serialize a HashMap<String, HashMap<String, String>>
     */
	  public static byte [] serializeHashMap(HashMap<String, HashMap<String, String>> map) throws IOException, ClassNotFoundException {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ObjectOutput objOut = new java.io.ObjectOutputStream(out);
	        objOut.writeObject(map);
	        objOut.close();
	        return out.toByteArray();
	    }

	  @SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, String>> deSerializeHashMap(byte [] data_in) throws IOException, ClassNotFoundException {
	        ByteArrayInputStream bosin = new ByteArrayInputStream(data_in);
	        ObjectInput in = new ObjectInputStream(bosin);
	        return (HashMap<String, HashMap<String, String>>) in.readObject();
    }
    public static boolean isGUID(String id) {
    	if (id == null || id.trim().length() != 36 || id.replace("-", "").length() != 32) return false;
    	return true;
	}
    public static boolean isValidEmailAddress(String email) {
    	if (email == null || email.trim().length() < 7 || email.indexOf("@") < 2) return false;
    	return true;
	}

    /*
     * make these strings with correct decimals
     */
	public static String toMoney(float amount) {
		return String.format("%.2f", amount);
	}
	public static String toMoney_b(int amount) {
		if (amount == 0) return "0.00";
		if (amount < 0) {
			amount = -amount;
			String res = String.format("%02d", (amount - ((amount/100)*100)));
			return  "-"+(amount/100) + "." + res;
		}
		String res = String.format("%02d", (amount - ((amount/100)*100)));
		return  ""+(amount/100) + "." + res;
	}
	// xx.xxxx => xx.xx
	public static String toMoney(String amount) {
		if (amount == null || amount.isEmpty()) {
			return "0.00";
		}
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(2, BigDecimal.ROUND_DOWN);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		return df.format(money);
	}
	// xx.xx => int
	public static int toMoney_int(String amount) {
		if (amount == null || amount.isEmpty()) {
			return 0;
		}
		try {
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(2, BigDecimal.ROUND_DOWN);
		money  = money.multiply(new BigDecimal("100"));
		return money.intValue();
		} catch (Throwable t) {
			return 0;
		}
	}
	// xx.xx => float
	public static float toMoney_float(String amount) {
		if (amount == null || amount.isEmpty()) {
			return 0;
		}
		try {
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(2, BigDecimal.ROUND_DOWN);
		return money.floatValue();
		} catch (Throwable t) {
			return 0;
		}
	}
	// xx.xx => float
	public static float toMoneyVal(String amount) {
		if (amount == null || amount.isEmpty()) {
			return (float)0;
		}
		try {
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(2, BigDecimal.ROUND_DOWN);
		return money.floatValue();
		} catch (Throwable t) {
			return 0;
		}
	}


	// xxxx => int
	public static int toMoneyVal_b(String amount) {
		if (amount == null || amount.isEmpty()) {
			return 0;
		}
		int f = 0;
		try {
			f = Integer.parseInt(amount);
		} catch (Throwable t) {
			return 0;
		}
		return f;
	}
	// xx.xxxx => xxxx
	public static String toMoneyftob(String amount) {
		if (amount == null || amount.isEmpty()) {
			return "0";
		}

		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(2, BigDecimal.ROUND_DOWN);
		money  = money.multiply(new BigDecimal("100"));

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);

		return df.format(money);
	}

	public static String fmtDouble(double value_d) {
		if (value_d == Double.MAX_VALUE) value_d = 0;
		if (value_d == Double.MIN_VALUE) value_d = 0;
		String r = String.format("%.6f", value_d);
		return r.replaceAll("\\.?0*$", "");
	}
	public static String fmtDouble2(double value_d) {
		if (value_d == Double.MAX_VALUE) value_d = 0;
		if (value_d == Double.MIN_VALUE) value_d = 0;
		String r = String.format("%.2f", value_d);
		return r.replaceAll("\\.?0*$", "");
	}

	// xx.xxxxxxxxxx => xx.xxxxx
	public static String toRatio(double amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		myBase_b = myBase_b.setScale(6, BigDecimal.ROUND_DOWN);
		return myBase_b.toString();
		} catch (Throwable t) {
			return "0.000000";
		}
	}
	// xx.xxxxxxxxxx => xx.xx
	public static String toRatio2(double amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		myBase_b = myBase_b.setScale(2, BigDecimal.ROUND_DOWN);
		return myBase_b.toString();
		} catch (Throwable t) {
			return "0.00";
		}
	}
	// xx.xxxxxxxxxx => xx.xx
	public static double toRatio2dec(double amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		myBase_b = myBase_b.setScale(2, BigDecimal.ROUND_DOWN);
		return myBase_b.doubleValue();
		} catch (Throwable t) {
			return 0;
		}
	}

	public static double toRatio6(double amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		myBase_b = myBase_b.setScale(6, BigDecimal.ROUND_DOWN);
		return myBase_b.doubleValue();
		} catch (Throwable t) {
			return 0;
		}
	}

	public static int toIntHex(String hex_amount) {
		int val = 0;
		try {
		val = Integer.parseInt(hex_amount, 16);
		} catch (Throwable t){}
		return val;
	}

	public static int toInt(String amount) {
		int val = 0;
		try {
		val = Integer.parseInt(amount);
		} catch (Throwable t){}
		return val;
	}
	public static long toLong(String amount) {
		long val = 0;
		try {
		val = Long.parseLong(amount);
		} catch (Throwable t){}
		return val;
	}
	public static double toDouble(String amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		return myBase_b.doubleValue();
		} catch (Throwable t) {
			return 0;
		}
	}
	public static double toDouble2(String amount) {
		try {
		BigDecimal myBase_b = new BigDecimal(""+amount);
		myBase_b = myBase_b.setScale(2, BigDecimal.ROUND_DOWN);
		return myBase_b.doubleValue();
		} catch (Throwable t) {
			return 0;
		}
	}
	public static BigInteger toBigInt(String v) {
		try {
			return new BigInteger(v);
		} catch (Throwable t) {}
		return null;
	}
	public static boolean compare(String s1, String s2) {
		if (s1 == null && s2 == null) return true;
		if (s1 == null || s2 == null) return false;
		return s1.equals(s2);
	}
	public static boolean contains(String big, String small) {
		if (big == null || small == null) return false;
		return big.contains(small);
	}

	/*
	 * format company name
	 */
	public static String phoneNormalize(String number) {
		if (number == null) {
			return null;
		}
		number = number.replaceAll("[^\\d]", "");
		return number.trim();
	}
	public static String emailNormalize(String email) {
		if (email == null) {
			return null;
		}
		email = email.toLowerCase();
		return email.trim();
	}
	public static String fmtProperWord(String word) {
		if (word == null) return null;
		if (word.length() == 1) return word.toUpperCase();
		String w = word.toLowerCase().trim();
		return w.substring(0, 1).toUpperCase() + w.substring(1, w.length());
	}
	public static String fmtInitial(String word) {
		if (word == null || word.isEmpty()) return null;
		return word.substring(0, 1).toUpperCase() + ".";
	}
	public static String fmtInitialNoDot(String word) {
		if (word == null || word.isEmpty()) return null;
		return word.substring(0, 1).toUpperCase();
	}

	public static boolean isValue(String val) {
		if (val == null || val.isEmpty()) return false;
		return true;
	}
	public static String getValue(String val) {
		if (val.equalsIgnoreCase("(null)")) return null;
		return val;
	}
	public static boolean getBoolean(String val) {
		if (val.equalsIgnoreCase("true")) return true;
		return false;
	}

	/*
	 * calculate a rate
	 */
	public static double calculateRate(int ret, int cost) {
		BigDecimal calc = calculateRateBigD(ret, cost);
		if (calc != null) {
			return calc.doubleValue();
		}
		return 0;
	}
	// (ret / cost) * 100
	public static BigDecimal calculateRateBigD(int ret, int cost) {
		BigDecimal calc = new BigDecimal(ret);
		if (cost > 0) {
			// get day rate
			calc = calc.divide(new BigDecimal(cost), 8, RoundingMode.HALF_UP);
			calc = calc.multiply(new BigDecimal("100"));
			calc = calc.setScale(2, BigDecimal.ROUND_DOWN);
			return calc;
		} else {
			return null;
		}
	}
	public static double calculateAnnualRate(int term, int cost, int fees) {
		BigDecimal calc = calculateAnnualRateBigD(term, cost, fees);
		if (calc != null) {
			return calc.doubleValue();
		}
		return 0;
	}
	public static String calculateAnnualRateStr(int term, int cost, int fees) {
		BigDecimal calc = calculateAnnualRateBigD(term, cost, fees);
		if (calc != null) {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			df.setGroupingUsed(false);
			return df.format(calc);
		}
		return "0.00";
	}
	public static BigDecimal calculateAnnualRateBigD(int term, int cost, int fees) {
		if (term < 1) {
			term = 1;
		}
		if (cost > 0 && cost > fees) {
			BigDecimal calc = new BigDecimal(fees);
			// get day rate
			calc = calc.divide(new BigDecimal(cost), 8, RoundingMode.HALF_UP);
			calc = calc.multiply(new BigDecimal("100"));
			calc = calc.divide(new BigDecimal(term), 8, RoundingMode.HALF_UP);
			calc = calc.multiply(new BigDecimal("365"));
			calc = calc.setScale(2, BigDecimal.ROUND_DOWN);
			return calc;
		} else {
			return null;
		}
	}


	// get days between to dates
	private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
	private static final int MILLISECONDS_IN_MIN = 1000 * 60;
	private static final int MILLISECONDS_IN_SEC = 1000;
	public static int getDaysBetween(Calendar startCal, Calendar endCal, String time_zone){
		if (startCal == null || endCal == null) {
			return 0;
		}
		Calendar sC = (Calendar)startCal.clone();
		Calendar eC = (Calendar)endCal.clone();
		sC = setTimeZone(sC, time_zone);
		eC = setTimeZone(eC, time_zone);
		sC.set(Calendar.HOUR_OF_DAY, 0);
		sC.set(Calendar.MINUTE, 1);
		sC.set(Calendar.SECOND, 0);
		eC.set(Calendar.HOUR_OF_DAY, 0);
		eC.set(Calendar.MINUTE, 1);
		eC.set(Calendar.SECOND, 0);

		//System.out.println("getDaysBetween: " + ServiceUtil.formatTimeString(sC) + " to " + ServiceUtil.formatTimeString(eC));

		long endTime = eC.getTimeInMillis();
		long startTime = sC.getTimeInMillis();
		return (int) ((endTime - startTime) / MILLISECONDS_IN_DAY);
	}
	public static int getDaysBetween(Calendar startCal, Calendar endCal){
		return getDaysBetween(startCal, endCal, "PST");
	}
	public static int getDaysBetween(long startCal, long endCal){
		return (int) ((endCal - startCal) / MILLISECONDS_IN_DAY);
	}



	/*
	 * check for sameDate
	 */
	public static boolean isSameDate(Calendar startCal, Calendar endCal, String time_zone){
		if (startCal == null && endCal == null) {
			return true;
		}
		if (startCal == null || endCal == null) {
			return false;
		}

		Calendar sd = (Calendar)startCal.clone();
		Calendar ed = (Calendar)endCal.clone();
		sd = setTimeZone(sd, time_zone);
		ed = setTimeZone(ed, time_zone);
		int sMonth = sd.get(Calendar.MONTH);
		int eMonth = ed.get(Calendar.MONTH);

		// same date and year at 1 min past start of day
		if (eMonth == sMonth && endCal.get(Calendar.DATE) == startCal.get(Calendar.DATE)
				&& endCal.get(Calendar.YEAR) == startCal.get(Calendar.YEAR)) {
			return true;
		}

		return false;
	}

	public static boolean isSameDate(Calendar startCal, Calendar endCal){
		return isSameDate(startCal, endCal, "PST");
	}
	public static int getHoursBetween(Calendar startCal, Calendar endCal, String time_zone){
		int min = getMinutesBetween(startCal, endCal, time_zone);
		if (min <= 0) {
			return 0;
		}
		return min / 60;
	}
	public static int getHoursBetween(Calendar startCal, Calendar endCal){
		return getHoursBetween(startCal, endCal, "PST");
	}
	public static int getMinutesBetween(Calendar startCal, Calendar endCal, String time_zone){
		if (startCal == null || endCal == null) {
			return 0;
		}
		Calendar sC = (Calendar)startCal.clone();
		Calendar eC = (Calendar)endCal.clone();
		sC = setTimeZone(sC, time_zone);
		eC = setTimeZone(eC, time_zone);
		long endTime = eC.getTimeInMillis();
		long startTime = sC.getTimeInMillis();
		return (int) ((endTime - startTime) / MILLISECONDS_IN_MIN);
	}
	public static int getMinutesBetween(Calendar startCal, Calendar endCal){
		return getMinutesBetween(startCal, endCal, "PST");
	}
	public static int getMinutesBetween(long startCal, long endCal){
		return (int) ((endCal - startCal) / MILLISECONDS_IN_MIN);
	}
	public static int getSecondsBetween(Calendar startCal, Calendar endCal, String time_zone){
		if (startCal == null || endCal == null) {
			return 0;
		}
		Calendar sC = (Calendar)startCal.clone();
		Calendar eC = (Calendar)endCal.clone();
		sC = setTimeZone(sC, time_zone);
		eC = setTimeZone(eC, time_zone);
		long endTime = eC.getTimeInMillis();
		long startTime = sC.getTimeInMillis();
		return (int) ((endTime - startTime) / MILLISECONDS_IN_SEC);
	}
	public static int getSecondsBetween(Calendar startCal, Calendar endCal){
		return getSecondsBetween(startCal, endCal, "PST");
	}
	public static int getSecondsBetween(long startCal, long endCal){
		return (int) ((endCal - startCal) / MILLISECONDS_IN_SEC);
	}

	public static boolean isBetween(Calendar date, Calendar start, Calendar end) {
		if (date == null || start == null || start == end) return false;

		if (isSameDate(date, start) || isSameDate(date, end)) return true;
		if (date.after(start) && date.before(end)) return true;
		return false;
	}
	public static String getDurration(Calendar startCal, Calendar endCa) {
		int sec = Gtil.getSecondsBetween(startCal, endCa);
		int min = sec / 60;
		sec -= min *60;
		return min + " min " +sec + " seconds";
	}

	// set this to the timezone
	public static String getTimeZoneStr(String time_zone) {
		if (time_zone != null && !time_zone.isEmpty()) {
			if (time_zone.equalsIgnoreCase("PST")) {
				time_zone = "America/Los_Angeles";
			} else if (time_zone.equalsIgnoreCase("CST")) {
				time_zone = "US/Central";
			} else if (time_zone.equalsIgnoreCase("MST")) {
				time_zone = "US/Mountain";
			} else if (time_zone.equalsIgnoreCase("EST")) {
				time_zone = "America/New_York";
			}
		}
		return time_zone;
	}
	private static Calendar setTimeZone(Calendar date, String time_zone) {
		if (date == null) {
			return null;
		}
		// set timezone
		if (time_zone != null && !time_zone.isEmpty()) {
			TimeZone tz = TimeZone.getTimeZone(getTimeZoneStr(time_zone));
			if (tz != null) {
				date.setTimeZone(tz);
			}
		}
		return date;
	}

	// get Months between to dates
	public static int getMonthsBetween(Calendar start, Calendar end) {
		if (start == null || end == null) return 0;

		int syear = start.get(Calendar.YEAR);
		int sMonth = start.get(Calendar.MONTH);
		int eyear = end.get(Calendar.YEAR);
		int eMonth = end.get(Calendar.MONTH);
	//	System.out.println("between: " + sMonth + "/" + syear + " > " + eMonth + "/" + eyear + "  == " + ((eyear - syear) * 12) + " M " + ((eMonth - sMonth) + 1));
		return ((eyear - syear) * 12) + ((eMonth - sMonth));
	}
	// get years between to dates
	public static int getYearsBetween(Calendar start, Calendar end) {
		if (start == null || end == null) {
			return 0;
		}
		int syear = start.get(Calendar.YEAR);
		int sMonth = start.get(Calendar.MONTH);
		int eyear = end.get(Calendar.YEAR);
		int eMonth = end.get(Calendar.MONTH);
		return ((eyear - syear) + ((eMonth - sMonth) + 1) / 12);
	}

	/*
	 * getUTCInstance()
	 */
	public static Calendar getUTCTime() {
	   Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	   //System.out.println("current: "+c.getTime());

	    TimeZone z = c.getTimeZone();
	    int offset = z.getRawOffset();
	    if(z.inDaylightTime(new Date())){
	        offset = offset + z.getDSTSavings();
	    }
	    int offsetHrs = offset / 1000 / 60 / 60;
	    int offsetMins = offset / 1000 / 60 % 60;

	    //System.out.println("offset hrs: " + offsetHrs + " offset min: " + offsetMins);

	    c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
	    c.add(Calendar.MINUTE, (-offsetMins));

	    //System.out.println("GMT Time: "+c.getTime());
	    return c;
	}
	public static Calendar getUTCTimePlus(int hours) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.HOUR, hours);
		return t;
	}
	public static Calendar getUTCTimePlusMinutes(int min) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.MINUTE, min);
		return t;
	}
	public static Calendar getUTCTimePlusDay(int days) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.DATE, days);
		return t;
	}
	public static Calendar getUTCTimeMinus(int hours) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.HOUR, -hours);
		return t;
	}
	public static Calendar getUTCTimeMinusMinutes(int min) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.MINUTE, -min);
		return t;
	}
	public static Calendar getUTCTimeMinusDay(int days) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.DATE, -days);
		return t;
	}
	public static Calendar getUTCTimeMinusWeek(int weeks) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.DATE, -(weeks*7));
		return t;
	}
	public static Calendar getUTCTimeMinusMonth(int Months) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.MONTH, -Months);
		return t;
	}
	public static Calendar getUTCTimeMinusYear(int years) {
		Calendar t = Gtil.getUTCTime();
		t.add(Calendar.YEAR, -years);
		return t;
	}
	public static Calendar convertToGmt(Calendar cal) {
		if (cal == null) {
			return null;
		}
		Date date = cal.getTime();
		TimeZone tz = cal.getTimeZone();

		//Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
		long msFromEpochGmt = date.getTime();

		//gives you the current offset in ms from GMT at the current date
		int offsetFromUTC = tz.getOffset(msFromEpochGmt);

		//create a new calendar in GMT timezone, set to this date and add the offset
		Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCal.setTime(date);
		gmtCal.add(Calendar.MILLISECOND, offsetFromUTC);

		return gmtCal;
	}


	// retains list order
	public static ArrayList<String> uniqueStringList(ArrayList<String> list) {
		if (list == null || list.size() < 2) return list;

		ArrayList<String> nl = new ArrayList<String>();
		Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
        	String o = iterator.next();
            if (!nl.contains(o)) nl.add(o);
          //  iterator.remove();
        }
        return nl;
	}


	public static byte[] getBytes(InputStream is) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		try {
			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buffer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buffer.toByteArray();
	}
	public static int indexOfAnyIgnoreCase(String test, String [] list) {
		if (test == null || list == null || list.length < 1) return -1;
		for (int i=0;i<list.length;i++) if (test.equalsIgnoreCase(list[i])) return i;
		return -1;
	}
	public static int indexOfContains(String test, String [] list) {
		if (test == null || list == null || list.length < 1) return -1;
		for (int i=0;i<list.length;i++) if (test.contains(list[i])) return i;
		return -1;
	}
	public static int indexOfStartsWith(String test, String [] list) {
		if (test == null || list == null || list.length < 1) return -1;
		for (int i=0;i<list.length;i++) if (test.startsWith(list[i])) return i;
		return -1;
	}
	public static int indexOfStartsWithIgnoreCase(String test, String [] list) {
		if (test == null || list == null || list.length < 1) return -1;
		String st = test.toLowerCase();
		for (int i=0;i<list.length;i++) if (st.startsWith(list[i])) return i;
		return -1;
	}
	public static int countLetters(String word) {
	    String onlyLetters = word.replaceAll("[^\\p{L}]", "");
	    return onlyLetters.length();
	}
	public static int upperCaseCount(String word) {
		int upper = 0;
		for (int i=0;i<word.length();i++) {
			if (Character.isUpperCase(word.charAt(i))) upper++;
		}
		return upper;
	}
	public static int lowerCaseCount(String word) {
		int upper = 0;
		for (int i=0;i<word.length();i++) {
			if (Character.isLowerCase(i)) upper++;
		}
		return upper;
	}
	public static int letterNumberCount(String word) {
		if (word == null) return 0;
		int upper = 0;
		for (int i=0;i<word.length();i++) {
			if (Character.isLetterOrDigit(word.charAt(i))) upper++;
		}
		return upper;
	}

	// must be classifer chars
	public static boolean checkClassifier(String classifier) {
		if (classifier == null || classifier.length() < 3 || classifier.length() > 30) return false;
		for (char c:classifier.toCharArray()) {
			if (c == '_') continue;
			if (Character.isLetter(c)) continue;
			return false;
		}
		return true;
	}

	// get number from roman numerals
    public static int romanToDecimal(String romanNumber) {
        int decimal = 0;
        int lastNumber = 0;
        String romanNumeral = romanNumber.toUpperCase();
        /* operation to be performed on upper cases even if user 
           enters roman values in lower case chars */
        for (int x = romanNumeral.length() - 1; x >= 0 ; x--) {
            char convertToDecimal = romanNumeral.charAt(x);
            switch (convertToDecimal) {
                case 'M':
                    decimal = processRNDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processRNDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processRNDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processRNDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processRNDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processRNDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processRNDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
                default:
                	return -1;
            }
        }
        return decimal;
    }
    private static int processRNDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }

	public static String addSpaceAfter(String text) {
		if (text == null) return text;
		text = Gtil.fixToText(text);
		if (text == null) return text;
		try {
	//  then add; [{()]];:,?!& sometimes .
	//	text = text.replaceAll("\\", " \\ "); //
		text = text.replaceAll("[!?&{()}\";\\[\\]|/=]", " $0 ").replaceAll("\\s+", " ");

		// if : or , followed by NOT 0-9
		text = text.replaceAll("(?<=[^0-9])([:,])", " $0 ");
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (text == null) return text;
		return text.trim();
	}
	public static String addSpaceAfterFix(String text) {
		text = addSpaceAfter(text);
		return Gtil.fixIDNameSys(text);
	}

	// split to words for mapping / adding
	public static String [] getMultiTokens(String text) {
		if (text == null) return null;
		text = addSpaceAfterFix(text);
		if (text == null) return null;
		return text.split("\\s+");
	}

	  // this should be optimized
	public static boolean isVowel(char c) {
		  return isVowel(c, false);
	}
	public static boolean isVowel(char c, boolean why) {
		    switch (c) {
		      case 'a':
		      case 'A':
		      case 'e':
		      case 'E':
		      case 'i':
		      case 'I':
		      case 'o':
		      case 'O':
		      case 'u':
		      case 'U':
		        return true;
		      case 'y':
		      case 'Y':
		    	  if (why) return true;
		       // if (pos != 0 && isVowel(pos - 1)) return false; // FIXME account for 'y'
		      default:
		        return false;
		    }
	  }
	  public static int countVowels(String word) {
		  return countVowels(word, false);
	  }
	  public static int countVowels(String word, boolean why) {
			int cnt = 0;
			for (char c:word.toCharArray()) {
				if (isVowel(c, why)) cnt++;
			}
			return cnt;
	 }
	public static boolean isConsonant(char c) {
		if (Character.isLetter(c)) return !isVowel(c);
		return false;
	}


	// hash to match initials.
	// W. A. S.|W. A S| W A S|w.a.s|WAS|w. a. s.|w a s| w a s.|NOT was
	public static int hashInitials(String word) {
		return getHashString(word, true, false).hashCode();
	}
	public static int hashAcronym(String word) {
		return getHashString(word, false, false).hashCode();
	}
	public static String getAcronymString(String txt) {
		String r = txt;
		r = r.trim().replaceAll("[.]", "");
		return r.toUpperCase();
	}
	public static String getAcronymStringDots(String txt) {
		String r = getAcronymString(txt);
		r = r.replaceAll("\\B", ".");
		return r + ".";
	}
	public static String getAbrString(String txt) {
		String r = txt;
		r = r.trim().replaceAll("[.]", "");
		return Gtil.fmtProperWord(r);
	}
	public static String getAbrStringDots(String txt) {
		return getAbrString(txt) + ".";
	}


	// create token to hash for looser matching based on params
	public static String getHashString(String word, boolean nocase, boolean keep_tokens) {
		if (word == null) return null;
		String str = word;
		str = str.trim();
		str = str.replaceAll("-", " ");		// no hyphens
		boolean isupper = false;
		int mt = 0, upper = 0, letters = 0, ls = 0; //, tokens = 1,

		str = str.replaceAll("(?<=[./\\&])(?!$)", " ");
		str = str.replaceAll("([\\&])", " $1").replaceAll("\\s+", " ");

		for (int i=0;i<str.length();i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) upper++;
			if (Character.isLetter(c)) {
				letters++;
				ls++;
			}
			if (!Character.isLetter(c) || i == (str.length()-1)) {
				if (ls > 1) mt++;
				if (ls > 0) ls = 0;
			}
		//	if (Character.isWhitespace(c)) tokens++;
		}

		if (ls > 1) mt++; // mt is 0 if all single letters
		if (letters == upper) isupper = true;

		// else keep them together (Jr. jr jr. / MR mr. Mr. Mr MR.)
		if (nocase) str = str.toLowerCase();


		str = str.replaceAll("\\.", "");		// no dots
		str = str.replaceAll("/", " ");			// slash same a space
		if (keep_tokens && mt > 0 && !(mt == 1 && isupper)) {
			// remove spaces IF just 1 char
			String t [] = str.split("\\s+");
			str = "";
			for (int i=0;i<t.length;i++) {
				if (t[i].length() == 1 || i == 0) str += t[i];
				else str += " " + t[i];
			}
			//str = str.replaceAll("\\B|\\b", " "); 	// no replace existing spaces
		} else {
			str = str.replaceAll(" ", ""); 			// no spaces
			str = str.replaceAll("\\B", " "); 	// no replace existing spaces
		}
		return str;
	}

	public static int findRowOfChar(String text, char cm) {
		int cnt = 0;
		for (int i=0;i<text.length();i++) {
			char c = text.charAt(i);
			if (c == cm) cnt++;
			else return cnt;
		}
		return 0;
	}


	////////////////////////////////////////////////////////
	// determine the type of token this is
	// what is this was mixed with checkType() as well ?
	// to many types ?
	// PER word: LOUD, proper, abr, letter, initials, hyphen..
	public static String testStringType(String text) {
		return testStringType(text, false);
	}
	public static String testStringType(String text, boolean prnt) {

		if (text.length() > 2) {
			char c = text.charAt(text.length()-1);
			char c2 = text.charAt(text.length()-2);
			if (c == '\'' && (c2 == 's' || c2 == 'S')) {	//s'
			// ?? posessive

			} else if ((c == 's' || c == 'S') && c2 == '\'') {	// 's
			// ?? posessive ? not alwais could be an xxx is
			}
		}


		int lower = 0, upper = 0, letters = 0, numbers = 0, tokens = 1, mt = 0, mtu = 0, mtd = 0, st = 0, std = 0, ls = 0,
				dot = 0, hyp = 0, slash = 0, cons = 0, vowels = 0;
		boolean fup = false, enddot = false, shyp = false, sslash = false;
		//String hash = getHashString(text, false, true);
		for (int i=0;i<text.length();i++) {
			char c = text.charAt(i);
			if (c == '.') {
				dot++;
				if (i == (text.length()-1)) enddot = true;
			} else if (c == '-' || c == '—' || c == '–') {
				if (i == 0) shyp = true; // starts with hyphen
				hyp++;
			} else if (c == '/') {
				if (i == 0) sslash = true; // starts with slash
				slash++;
			} else if (Character.isLetter(c)) {
				if (isConsonant(c)) cons++;
				if (Gtil.isVowel(c, false)) vowels++;
				else if (i == (text.length()-1)) if (Gtil.isVowel(c, true)) vowels++;

				if (Character.isLowerCase(c)) lower++;
				else if (Character.isUpperCase(c)) {
					upper++;
					if (i == 0) fup = true;
					if (ls == 0) mtu++;
				}
				letters++;
				ls++;
			} else if (Character.isDigit(c)) {
				numbers++;
			} else if (Character.isWhitespace(c)) {
				tokens++;
			}
			if (!Character.isLetter(c) || i == (text.length()-1)) {
				if (ls > 1) {
					if (c == '.') mtd++; // multi-letter dot "Mr."
					mt++; // multi-letter
				} else if (ls > 0) {
			//		System.out.println("     ST["+c+" / "+i+"] ls: " + ls + " mt:"+mt);
					if (c == '.') std++; // single letter dot "H."
					st++; // single letter
				}
				ls = 0;
			}
		}

		if (letters == 0) {
			if (numbers > 0) {
				if (numbers == text.length()) return "number";
				if (shyp && numbers == (text.length()-1)) return "number"; // negative number
				return "identifier";
			} else return "symbol";
		}

		String pr = "";
		if (prnt) {
			String ts = getHashString(text, true, true);
			pr = "["+text+"]["+ts+"]("+text.length()+")("+tokens+") l:" + letters + " n:"+numbers
					+ " U:"+upper+ " l:"+lower + " mt:"+mt+ " mtu:"+mtu+ " mtd:"+mtd+ " st:"+st+ " std:"+std+ " .="+dot+ " -="+hyp+ " /="+slash ;
		}
		if (shyp) {
			return "start_hyphen";
		}
		if (sslash) {
			return "start_slash";
		}

		if (tokens > 1) {

			if (mt == 0 && st > 0 && numbers == 0 && letters > 1) {
				if (prnt) System.out.println("   testSTR[acronym]"+pr);
				return "acronym"; // T E S T / t e s t
			}
			String tks [] = Gtil.getMultiTokens(text);
			String fw = testStringType(tks[0]);

			// initials (H.G. Wells, HG Wells, h.g. wells, h.g wells, h. wells, H wells)
			if (tks.length == 2 && (dot > 0 || upper > tks.length || st > 0 || std > 0 || fw.equals("abr"))) {
				if (fw.equals("abr")) {
					if (letters == upper) {
						if (prnt) System.out.println("   testSTR[abr_loud_phrase]"+pr);
						return "abr_loud_phrase";
					}
					if (mtu == tks.length) {
						if (dot == 0) {
							if (prnt) System.out.println("   testSTR[proper_phrase]"+pr);
							return "proper_phrase";
						}
						if (prnt) System.out.println("   testSTR[abr_proper_phrase]"+pr);
						return "abr_proper_phrase";
					}
					if (prnt) System.out.println("   testSTR[abr_phrase]"+pr);
					return "abr_phrase";
				}
				if (fw.equals("initials")) {
					if (mt <= mtu) {
						if (prnt) System.out.println("   testSTR[initials_proper_phrase-]"+pr);
						return "initials_proper_phrase";
					}
					if (prnt) System.out.println("   testSTR[initials_phrase-]"+pr);
					return "initials_phrase";
				}
				if (dot != 0) {
					if (mt <= mtu) {
						if (prnt) System.out.println("   testSTR[initials_proper_phrase]"+pr);
						return "initials_proper_phrase";
					}
					if (prnt) System.out.println("   testSTR[initials_phrase]"+pr);
					return "initials_phrase";
				}
			}

			// initials (Hugo G. Wells, Hugo g wells)
			if (tks.length == 3 && mt > 0 && mt == mtu && (st > 0 || std > 0)) {
				if (prnt) System.out.println("   testSTR[initials_mid_proper_phrase]"+pr);
				return "initials_mid_proper_phrase";
			}
			if (tks.length == 3 && mt > 0 && (st > 0 || std > 0)) {
				if (prnt) System.out.println("   testSTR[initials_mid_phrase]"+pr);
				return "initials_mid_phrase";
			}
			if (numbers == 0 && mt == tks.length) {
				if (upper == letters) {
					if (prnt) System.out.println("   testSTR[loud_phrase]"+pr);
					return "loud_phrase"; // all uppercase (STOP THAT)
				}
				if (mtu == tks.length && upper == tks.length) {
					if (prnt) System.out.println("   testSTR[proper_phrase]"+pr);
					return "proper_phrase";  // all start with upper (The White House)
				}
				if (mtu == tks.length && upper > tks.length) {
					// some acronyms / etc in there too..
					if (prnt) System.out.println("   testSTR[proper_phrase+]"+pr);
					return "proper_phrase";
				}
				//if (upper > 0 && mtu == upper && fup && (mtu+1) == mt) {
				//	if (prnt) System.out.println("   testSTR[proper_phrase-]"+pr);
				//	return "proper_phrase"; // some upper start.. (Univerisity of Anabella)
				//}
				if (upper > 0 && mtu == upper) {
					if (prnt) System.out.println("   testSTR[mixed_phrase]"+pr);
					return "mixed_phrase"; // some upper start.. (Bob's white rabbit)
				}
				if (upper > 0) {
					if (prnt) System.out.println("   testSTR[moron_phrase]"+pr);
					return "moron_phrase"; // some upper not start.. (Bob's wHite raBBit or Bob's SoHo Apt)
				}
				if (prnt) System.out.println("   testSTR[phrase]"+pr);
				return "phrase";  // just words (white rabbit)
			}
			if (tokens <= mtu && mt <= mtu && lower > 0) {
				if (prnt) System.out.println("   testSTR[proper_phrase++]"+pr);
				return "proper_phrase";
			}
			if (lower == 0 && letters > 1 && upper == letters) {
				if (prnt) System.out.println("   testSTR[acronym-]"+pr);
				return "acronym"; // T E S T / t e s t
			}

			if (prnt) System.out.println("   testSTR[phrase-]"+pr);
			return "phrase"; // what would this bee?
		} else {
			if (letters == 1 && text.length() == 1) { // just one letter
				if (prnt) System.out.println("   testSTR[letter]"+pr);
				return "letter";
			}
			if (numbers > 0 && letters > 0 && (letters + numbers) == text.length()) {
				if (text.length() < 7) return "alpha_num_short"; // ix300, B17
				return "alpha_num";
			}

			// Acronym  (NASA, USA)
			// 1 token, all upercase, match acronym alias
			if (upper == text.length()) {
				if (prnt) System.out.println("   testSTR[acronym]"+pr);
				return "acronym";
			}

			// initials (U.S.A.)
			// 1 token, all single letter, have dots, match initials alias
			if (mt == 0 && upper > 0 && lower == 0 && dot >= 2) {
				if (prnt) System.out.println("   testSTR[acronym]"+pr);
				return "initials";
			}
			if (mt == 0 && st == letters && dot > 0) {
				if (prnt) System.out.println("   testSTR[initials]"+pr);
				return "initials"; // t.e.s.t. / t e s.
			}
			// abr (Mrs., Ca, Jan)
			// 1 token, start upper, possible dot, match abr alias
		//	testSTR[proper][Mr.][mr](3)(1/1) l:2 n:0 U:1 l:0 mt:1 mtu:1 mtd:1 st:0 std:0 .=1 -=0 /=0
			if (fup && mt == 1 && upper == 1 && lower >= 1 && (text.length() < 3 || enddot)) {
				if (prnt) System.out.println("   testSTR[abr]"+pr);
				return "abr";
			}
			if (text.length() == 2 && vowels == 0 && letters == 2) {
				if (upper == 0) return "abr"; // mt, st, vt, ct
				else if (upper == 2) return "acronym";
			}

			if (text.length() > 3 && dot > 0 && !enddot && mt >= 1 && letters > 2 && (dot + letters) == text.length()) {
				char c = text.charAt(text.length()-1);
				char cs1 = text.charAt(0);
				char cs2 = text.charAt(1);
				if (Character.isLetter(cs1) && Character.isLetter(cs2) && Character.isLetter(c)) {
					if (prnt) System.out.println("   testSTR[fix_period]"+pr);
					return "fix_period";
				} else {
					// many things here are wrong.. but so many could be a standard format..
				}
			}

			// hyphen word (this-and-that)
			if (hyp > 0) {
				if (hyp == 1 && fup && letters == upper && letters < 4 && numbers > 0 && (text.length() == letters + numbers+1)) {
					// uppercase & numbers "B-17", "G-7"
					if (text.length() < 7) return "alpha_num_short";
					return "alpha_num";
				}
				if (prnt) System.out.println("   testSTR[hyphen_word]"+pr);
				return "hyphen_word";
			}
			if (fup && text.length() == letters && letters == cons && letters < 5) {
				if (prnt) System.out.println("   testSTR[abr-]"+pr);
				return "abr";
			}
			if (mt == 0 && st == letters && slash > 0) {
				if (prnt) System.out.println("   testSTR[acronym]"+pr);
				return "acronym"; // n/a / t/e/s
			}

			// abr_multi (SoMa, SoFi, MassiveAttribute, NRPRadio
			// 1 token, multiple uppercase segments (usually sylobol based break out)
			if (upper > 1 && lower >= 1 && dot == 0) {
				if (isProperSirName(text, fup, letters, upper, lower, numbers, hyp)) return  "proper"; // "O'Hara"/D'Angelo.. etc"
				if (prnt) System.out.println("   testSTR[abr_multi]"+pr);
				return "abr_multi";
			}

			// initials and Name (H.G.Wells)
			// 1 token, multiple dots
			if (mt == 1 && dot > 0 && std > 0) {
				if (prnt) System.out.println("   testSTR[initials_name]"+pr);
				return "initials_name";
			}
			if ((upper + dot) == text.length() && dot == 1 && enddot) {  // JR. MS. MR.
				if (prnt) System.out.println("   testSTR[abr]"+pr);
				return "abr";
			}
			// dr. mr.
			if ((letters + dot) == text.length() && dot == 1 && enddot && letters < 5) {
				if (prnt) System.out.println("   testSTR[abr]"+pr);
				return "abr";
			}

			if (mtu == 1 && upper == 1 && lower > 1 && (upper + lower) == text.length()) {
				if (prnt) System.out.println("   testSTR[proper]"+pr);
				return "proper";
			}

			if (letters > 0 && ((lower == 0 && letters == upper) || ( letters > 2 && lower == 1 && text.contains("s")))) {
				// acronym OR acronym plural
				if (prnt) System.out.println("   testSTR[acronym-1]"+pr);
				return "acronym";
			}

			if (enddot) {
				if (prnt) System.out.println("   testSTR[abr]"+pr);
				return "abr";
			}
			if (upper > 0 && lower > 0 && numbers == 0) { // McDonald
				if (isProperSirName(text, fup, letters, upper, lower, numbers, hyp)) return  "proper"; // "O'Hara"/D'Angelo.. etc"
				return "abr_multi";
			}
			if (numbers > 0) { // deGrasse
				return "alpha_num_short";
			}
			if (upper > 0 && upper == letters) { // 'N 'S N'T
				return "proper";
			}

			// single word (this)
			// 1 token, nothing speical
			if (prnt) System.out.println("   testSTR[word]"+pr);
			return "word";
		}
	}
	// check for proper sirname format.. even if not Proper format
	public static boolean isProperSirName(String text, boolean fup, int letters, int upper, int lower, int numbers, int hpy) {
		if (numbers > 0 || upper > 2 || letters < 2) return false;
		if (fup && upper == 2) {
			if (text.startsWith("Mc") && letters > 4) return true; // "McMuller".. etc"
			if (text.startsWith("Mac") && letters > 5) return true; // "MacCarthy".. etc"
			if (Character.isUpperCase(text.charAt(2)) && text.charAt(1) == '\''  && letters > 4) return true; // "O'Hara"/D'Angelo.. etc"
		} else if (!fup && upper == 1) {
			if (text.startsWith("de") && Character.isUpperCase(text.charAt(2))  && letters > 4) return true; // deGause
		}
		return false;
	}

	public static int matchHashString(String testStr, String refString, boolean nocase, boolean keep_tokens) {
		// return closeness of them...
		String ts = getHashString(testStr, nocase, keep_tokens);
		String rs = getHashString(refString, nocase, keep_tokens);
		boolean res = (ts.hashCode() == rs.hashCode());
		boolean res1 = ts.equals(rs);
		//String tst1 = testStringType(testStr);
		String tst2 = testStringType(refString);

		//System.out.println("MA["+tst1+"]["+testStr+" > "+ts+"]  ["+tst2+"]["+refString+" > "+rs+"]  " + res + " / " + res1);
		//System.out.println("["+tst2+"]["+refString+" > "+rs+"]  " + res + " / " + res1);
		if (res) return 1;
		return 0;
	}

	public static void testMatch() {
		// ISSUE: token matching 1 at a time will not work well for such strings.
		// must address this in the core to check accross tokens that resolve to letters / etc. TEST TEST TEST
//		matchHashString("test", "test", true, false);
//		matchHashString("test", "Test", true, false);
		matchHashString("test", "L", true, false);
		matchHashString("test", "l", true, false);
		matchHashString("test", "-", true, false);
		matchHashString("test", "9", true, false);

		matchHashString("test", "M.", true, false); // ??
		matchHashString("test", "It", true, false); // abr
		matchHashString("test", "the EPA", true, false);
		matchHashString("test", "The Tick Project", true, false);

		matchHashString("test", "President George W. Bush", true, false);
		matchHashString("test", "the future's", true, false);
		matchHashString("test", "U.S. Ambassador", true, false);


		matchHashString("test", "ct", true, false); // abr
		matchHashString("test", "dr.", true, false); // abr
		matchHashString("test", "TEST.", true, false); // abr
		matchHashString("MR", "Mr.", true, true);
		matchHashString("MR", "Miss", true, true);
		matchHashString("test", "TEST", true, false);

		matchHashString("test", "T.E.S.T.", true, false);
		matchHashString("test", "t.e.s.t.", true, false);
		matchHashString("test", "t.e.s.t", true, false);
		matchHashString("test", "t/e/s/t", true, false); // acronym

		matchHashString("test", "T E S T", true, false);
		matchHashString("test", "t e s t", true, false); // acronym VS initials
		matchHashString("test", "T. E. S. T.", true, false);

		matchHashString("test", "t-e-s-t", true, false); // word
		matchHashString("test", "Te. st.", true, false); // initials_phrase ??
		matchHashString("test", "TE st.", true, false); //initials_phrase ??
		matchHashString("te-st", "te st", true, true);
		matchHashString("Washington D.C.", "Washington D.C.", true, true);
		matchHashString("University of Massachusetts", "University of Massachusetts", true, true);
		matchHashString("Li Min", "Li Min", true, true);
		matchHashString("Li Min", "FactSet Research Systems Inc.", true, true);
		matchHashString("Li Min", "President Donald Trump's", true, true);
		matchHashString("Li Min", "The Anti-Defamation League", true, true);

		matchHashString("mr dog", "Mr. Dog", true, true); // initials_phrase FIXME abr_phrase ??
		matchHashString("MR dog", "MR. DOG", true, true); // initials_phrase FIXME abr_phrase ??
		matchHashString("MR dog", "MR Dog", true, true);//moron_phrase FIXME
		matchHashString("MR dog", "MR dog", true, true); //moron_phrase FIXME
		matchHashString("MR dog", "Mrdog", true, true); // word
		matchHashString("MR dog", "mr dog", true, true); // phrase
		matchHashString("mY Dog", "My Dog Jack", true, true); // proper_phrase
		matchHashString("mr dog", "Mr. John Dog", true, true);
		matchHashString("mr dog", "MR. John Dog", true, true);
		matchHashString("mr dog", "Mr. HG Dog", true, true);
		matchHashString("mr dog", "Mr. H.G Dog", true, true);

		matchHashString("H. G. Wells", "H. G. Wells", true, true);
		matchHashString("H. Gwells", "h. wells", true, true);
		matchHashString("H. Gwells", "H. Wells", true, true);
		matchHashString("H. Gwells", "h g wells", true, true);
		matchHashString("HG Wells", "HG wells", true, true);
		matchHashString("HG Wells", "HG. Wells", true, true);
		matchHashString("h.g.wells", "h.g.wells", true, true);		// BUG, first should have space
		matchHashString("H.G.Wells", "H.G.wells", true, true);		// BUG, first should have space
		matchHashString("H. G.Wells", "H.G. wells", true, true);	// BUG, first should have space

		matchHashString("n/a", "NA", true, true);
		matchHashString("n/a", "n/a", true, true);
		matchHashString("and/or", "and/or", true, true); // FIXME New type

		matchHashString("PGE", "PG&E", true, true); // BUG, spaces around '&'

		matchHashString("P.G.&E.", "P.G.&E.", true, true);
		matchHashString("P.G.&.E.", "P.G.&.E.", true, true);
		matchHashString("PG & E", "PG & E", true, true);
		matchHashString("PG/E", "PG /E", true, true);
		matchHashString("PG/E", "PG / E", true, true);
		matchHashString("PG/E", "PG/E", true, true);
		matchHashString("PG/E", "what-ever", true, true);
	}


    /////////////////////////////////////////////////////////////////
    // cleanup of text
	public static String fixNameSys(String name) {
		if (name == null || name.isEmpty()) return null;
		if (name.length() == 0) return "";
		name = name.replaceAll("/", "#47;");
		name = name.replaceAll("\\?", "#63;");
		//name = name.replaceAll("%", "#37;");
		name = name.replaceAll("&", "#38;");
		name = name.replaceAll("\\|", "#124;");
		name = name.replaceAll("=", "#61;");
		return name.trim();
	}

	public static String fixToText(String name) {
		if (name == null || name.isEmpty()) return null;
		if (name.length() == 0) return "";
		name = name.replaceAll("#47;", "/");
		name = name.replaceAll("#63;", "?");
		name = name.replaceAll("#38;", "&");
		name = name.replaceAll("#58;", ":");
		name = name.replaceAll("#124;", "|");
		name = name.replaceAll("#61;", "=");
		name = name.replaceAll("#33;", "!");
		name = name.replaceAll("#37;", "%");

		name = name.replaceAll("#40;", "(");
		name = name.replaceAll("#41;", ")");
		name = name.replaceAll("#91;", "[");
		name = name.replaceAll("#93;", "]");
		return name.trim();
	}

	public static String fixIDNameSys(String name) {
		if (name == null || name.isEmpty()) return null;
		if (name.length() == 0) return "";
		name = name.replaceAll(":", "#58;");
		name = name.replaceAll("é", "e");
		// issue?
		name = name.replaceAll("\\(", "#40;");
		name = name.replaceAll("\\)", "#41;");
		name = name.replaceAll("\\[", "#91;");
		name = name.replaceAll("\\]", "#93;");
		name = name.replaceAll("\\!", "#33;");
		return fixNameSys(name);
	}

	//////////////////////////////////////
	// textClassified parser
	// word:classifier:ttype   -- classifier
	// word::classifer:ttype   -- classifier is ancestor of match
	// %any%:classifier:ttype  -- any word will match
	// word                    -- just match text
	// word:classifier         -- just the word and classifier match
	// %refs%:classifier       -- use string or string list
	// %anyall%:classifier:ttype  -- any word will match AND use /refs AND /rproper
	// %none% ... nothing here.
	public static String getTokenWord(String token) {
		  String word = token;
		  int cidx = token.indexOf(":");
		  if (cidx > 0) {
			  word = word.substring(0, cidx);
		  }
		  if (word.equals("%any%")) word = null;
		  //if (word.equals("%any%")) word = null;
		  return word;
	}
	public static String getTokenClassifier(String token) {
		  String classifier = null;
		  int cidx = token.indexOf(":");
		  if (cidx > 0) {
			  int nidx = token.indexOf(":", (cidx+1));
			  if (nidx > 0 && nidx == (cidx+1)) {
				  nidx = token.indexOf(":", (nidx+1));
			  }
			  if (nidx > 0) {
				  classifier = token.substring((cidx+1), nidx);
			  } else {
				  classifier = token.substring((cidx+1), token.length());
			  }
			  if (classifier.equalsIgnoreCase("%any%")) classifier = null;
		  }
		  return classifier;
	}
	public static String getTokenTtype(String token) {
		  String ttype = null;
		  int cidx = token.indexOf(":");
		  if (cidx > 0) {
			  int nidx = token.indexOf(":", (cidx+1));
			  if (nidx > 0 && nidx == (cidx+1)) {
				  nidx = token.indexOf(":", (nidx+1));
			  }
			  if (nidx > 0) {
				  ttype = token.substring((nidx+1), token.length());
			  }
		  }
		  return ttype;
	}
	public static String getTokenTag(String token) {
		// text : classifier : ttype : tag : pos
		  String pa [] = token.split(":");
		  boolean anc = getTokenIsAncestor(token);
		  int pos = 3;
		  if (anc) pos = 4;
		  if (pa.length > pos) return pa[pos];
		  return null;
	}
	public static String getTokenPOS(String token) {
		// text : classifier : ttype : tag : pos
		  String pa [] = token.split(":");
		  boolean anc = getTokenIsAncestor(token);
		  int pos = 4;
		  if (anc) pos = 5;
		  if (pa.length > pos) return pa[pos];
		  return null;
	}
	public static boolean getTokenIsAncestor(String token) {
		if (token.contains("::")) return true;
		return false;
	}
	public static int getTokensCount(String textclassified) {
		if (textclassified == null) return 0;
		String tokens [] = textclassified.trim().split("\\s+");
		return tokens.length;
	}
	public static boolean getTokensIsAny(String token) {
		if (token == null || token.isEmpty() || token.equalsIgnoreCase("%any%")) return true;
		return false;
	}
	public static boolean getTokensIsNone(String token) {
		if (token == null || token.isEmpty() || token.equalsIgnoreCase("%none%")) return true;
		return false;
	}
	public static boolean getTokensIsAnyAll(String token) {
		if (token.equalsIgnoreCase("%anyall%")) return true;
		return false;
	}
	public static String getTokensPath(String token) {
		if (token == null || token.isEmpty() || !token.startsWith("%") || !token.endsWith("%") || token.equalsIgnoreCase("%any%")) return null;
		return token.substring(1, token.length()-1);
	}

	public static String getTextClassified(String string) {
		if (string == null) return null;
		if (string.startsWith("%final%")) {
			int idx = string.indexOf(" ");
			if (idx < 0) return null;
			return string.substring(idx+1, string.length());
		}
		return string;
	}
	public static String getTextClassifiedClassifier(String string) {
		if (string == null) return null;
		if (string.startsWith("%final%")) {
			int idx = string.indexOf(" ");
			if (idx < 0) return null;
			return string.substring(8, idx);
		}
		return null;
	}

	// get the basic phrase from the classiied text
	public static String getTokensBasic(String textclassified) {
		if (textclassified == null) return null;
		textclassified = getTextClassified(textclassified);
		String tokens [] = textclassified.split("\\s+");
		String basic_phrase = "";

	   for (int i=0;i<tokens.length;i++) {
		  String word = Gtil.getTokenWord(tokens[i]);
		  // build just the phrase
		  basic_phrase += word;
		  if (i != (tokens.length-1)) {
			  if (Gtil.indexOfAnyIgnoreCase(word, Gtil.no_space) < 0) basic_phrase += " ";
		  }
	   }

		return basic_phrase;
	}

	public static String getFileExtension(String fileName){
		return fileName.substring(
				fileName.lastIndexOf('.') + 1);
	}

}


