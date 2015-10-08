package com.github.jobop.anylog.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtils {
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(new FileInputStream(new File(System.getProperty("appHome") + "/config.properties")));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public static String getStringValue(String key) {
		return properties.getProperty(key);
	}

	public static int getIntValue(String key) {
		String stringValue = getStringValue(key);
		int value = 0;
		if (null != stringValue) {
			try {
				value = Integer.valueOf(stringValue);
			} catch (Exception e) {
			}
		}
		return value;
	}
}
