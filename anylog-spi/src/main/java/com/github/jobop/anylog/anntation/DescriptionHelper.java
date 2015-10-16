package com.github.jobop.anylog.anntation;

import java.lang.reflect.Field;

public class DescriptionHelper {

	public static String secutrityControl(Class<?> targetClass, String fieldName) {
		Field[] fields = targetClass.getDeclaredFields();
		for (Field field : fields) {// 检查标签注释字段
			if(field.getName().equals(fieldName)){
				Description descriptionAnnotation = field.getAnnotation(Description.class);
				if (null != descriptionAnnotation) {
					return descriptionAnnotation.value();
				}
			}
		}

		return "";
	}
}
