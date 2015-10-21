package com.github.jobop.anylog.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeRecorder {
	
	private static Map<String, Long> container = new ConcurrentHashMap<String, Long>();

    public static void start(String name) {
        container.put(name, System.currentTimeMillis());
    }

    public static String end(String name) {
        Long beginTime = container.get(name);
        if (beginTime == null) {
            return "-1";
        } else {
            container.remove(name);
        }
        Long endTime = System.currentTimeMillis();
        return Long.toString(endTime - beginTime);
    }

}
