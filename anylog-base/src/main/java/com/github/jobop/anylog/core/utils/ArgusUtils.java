package com.github.jobop.anylog.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ArgusUtils {
	public static final String PORT = "port";
	public static final String PROVIDERS = "providers";
	public static final String SYSTEM_JARS = "systemJars";

	public static Map<String, String> args2map(String args) {
		if (args == null) {
			args = "";
		}
		String[] pairs = args.split(",");

		Map<String, String> argMap = new HashMap<String, String>();
		for (String s : pairs) {
			int i = s.indexOf('=');
			String key, value = "";
			if (i != -1) {
				key = s.substring(0, i).trim();
				if (i + 1 < s.length()) {
					value = s.substring(i + 1).trim();
				}
			} else {
				key = s;
			}
			argMap.put(key, value);
		}

		return argMap;
	}

	public static String map2argus(Map<String, String> argMap) {
		if (null == argMap || argMap.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : argMap.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");

		}
		return sb.toString().substring(0, sb.length() - 1);
	}

}
