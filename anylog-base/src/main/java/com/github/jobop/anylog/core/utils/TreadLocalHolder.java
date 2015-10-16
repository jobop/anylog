package com.github.jobop.anylog.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TreadLocalHolder {
	private static ThreadLocal<List<Long>> elapsedTimeList = new ThreadLocal<List<Long>>(){
		@Override
		protected List<Long> initialValue() {
			return new ArrayList<Long>();
		}
	};
	
	private static ThreadLocal<SimpleDateFormat> simpleDateformat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	public static void setElapsedTime(){
		List<Long> list = elapsedTimeList.get();
		list.add(System.currentTimeMillis());
	}
	
	public static void removeElapsedTime(){
		elapsedTimeList.remove();
	}
	
	public static void showElapsedTime(){
		int num = 1;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < elapsedTimeList.get().size(); i++) {
			if(i == 0){
				sb.append("start time:"+ simpleDateformat.get().format(new Date(elapsedTimeList.get().get(0))));
				sb.append("\n");
			}else{
				sb.append("flag " + (num++)+":");
				sb.append((elapsedTimeList.get().get(i)-elapsedTimeList.get().get(i-1)));
				sb.append("\n");
			}
		}
		
		sb.append("end time:"+ simpleDateformat.get().format(new Date()));
		System.out.println(sb.toString());
		removeElapsedTime();
	}
}
