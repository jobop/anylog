package com.github.jobop.anylog.tools;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.jobop.anylog.common.fastjson.JSON;

public class MacroUtils {
	public static Map<String, String> mapping = new HashMap<String, String>();
	static {
		mapping.put("$sysout", "System.out.println");
		mapping.put("$trace", "com.github.jobop.anylog.tools.Trace.trace");
		mapping.put("$json", "com.github.jobop.anylog.common.fastjson.JSON.toJSONString");
	}

	public static String translate(String sourceCode) {
		if (null == sourceCode) {
			return sourceCode;
		}
		String distCode = sourceCode;
		for (Entry<String, String> entry : mapping.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			distCode = distCode.replace(key, value);
		}
		return distCode;
	}

	public static <T> T translate(T obj) {
		if (null == obj) {
			return null;
		}

		Class<?> clazz = obj.getClass();

		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();

			if (methodName.startsWith("get") && !methodName.equals("getClass") && String.class.isAssignableFrom(method.getReturnType())) {
				try {
					String value = (String) method.invoke(obj, new Object[] {});
					String newValue = translate(value);
					Method setter = clazz.getMethod(methodName.replaceFirst("get", "set"), String.class);
					setter.invoke(obj, new Object[] { newValue });

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return obj;
	}

	public static String safeInsertTemplate(String sourceCode) {
		if (null == sourceCode || "".equals(sourceCode)) {
			return "";
		} else {
			return "try{" + sourceCode + "}catch(Throwable e){e.printStackTrace();}";
		}

	}

}
