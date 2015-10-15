package com.github.jobop.anylog.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class ArgusUtils {
	public static final String PORT = "port";
	public static final String PROVIDERS = "providers";
	public static final String SYSTEM_JARS = "systemJars";

	public static Map<String, String> args2map(String args) {
		if (StringUtils.isEmpty(args)) {
			return new HashMap<String, String>();
		}
		return Splitter.on(",").trimResults().withKeyValueSeparator("=").split(args);
	}

	public static String map2argus(Map<String, String> argMap) {
		if (null == argMap || argMap.size() == 0) {
			return "";
		}
		
		return Joiner.on(",").withKeyValueSeparator("=").join(argMap);
	}
	
	public static void main(String[] args) {
		String argsStr = "";
		System.out.println(args2map(argsStr));
	}
}
