package com.github.jobop.anylog.anntation;

import java.lang.reflect.Field;
import java.util.Set;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.tools.MacroUtils;

public class SecurityControlHelper {
	
	public static boolean secutrityControl(TransformDescriptor descriptor){
		Class<? extends TransformDescriptor> targetClass = descriptor.getClass();
		Field[] fields = targetClass.getDeclaredFields();
		for (Field field : fields) {//检查标签注释字段
			SecurityControl securityControlForField = field.getAnnotation(SecurityControl.class);
			if(null != securityControlForField && securityControlForField.control()){
				if(!checkFieldSecurityControl(field,descriptor)){
					return false;
				}
			}
		}
				
		return true;
	}

	private static boolean checkFieldSecurityControl(Field field, TransformDescriptor descriptor) {
		try {
			field.setAccessible(true);
			Object object = field.get(descriptor);
			Set<String> set = MacroUtils.mapping.keySet();
			for (String string : set) {
				if(object instanceof String ){
					String insertCode = (String) object;
					if(insertCode.contains(string)){
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return false;
	}
}
