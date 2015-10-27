package com.github.jobop.anylog.core.utils;

public class ClassUtils {
	public static boolean checkClassExist(String className) {
		if (null == className || className.equals("")) {
			return false;
		}
		Class<?> clazz = null;
		// 不包含通配符才检查，包含就不检查了
		if (className.contains("*")) {
			return true;
		} else {
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return clazz != null;

	}
}
