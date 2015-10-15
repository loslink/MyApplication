package com.itlao.utils.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class DateUtil {
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	/**
	 * 时锟斤拷锟绞斤拷锟?
	 * 
	 * @param time
	 * @return
	 */
	public static String parseDate(int time) {
		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format("%02d:%02d", minute, second);
	}

	
	/**
	 * 时锟斤拷锟?
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getTimeDiff(Date startTime, Date endTime) {
		Long startTimeNum = startTime.getTime();

		Long endTimeNum = endTime.getTime();

		Long timeDiff = (endTimeNum - startTimeNum) / 1000;
		return timeDiff;
	}
	
	public static String parse2Str(JSONObject obj){
		String result = "";
		try {
			if (obj != null) {
				result = obj.getInt("month")+"-"+obj.getInt("date")
				+" "+obj.getInt("hours")+":"+obj.getInt("minutes")+":"+obj.getInt("seconds");
				
			}
		} catch (Exception e) {
			Log.e("DateUtil", e.getMessage());
		}
		return result;
	}
	
	public static String parse2DateStr(JSONObject obj){
		String result = "";
		try {
			if (obj != null) {
				result = obj.getInt("month")+"-"+obj.getInt("date");
				
			}
		} catch (Exception e) {
			Log.e("DateUtil", e.getMessage());
		}
		return result;
	}
	
	public static String fmtTime(long time) {
        if (0 == time) {
        	time=System.currentTimeMillis();
            return mDateFormat.format(new Date(time));
        }
	    return mDateFormat.format(new Date(time));
	 }
}
