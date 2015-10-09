package com.github.jobop.anylog.spi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解可用于用户的spi实现之上，作为实现的描述和具体字段的描述
 * 
 * @author nevynzheng
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SpiDesc {
	/**
	 * spi描述
	 * 
	 * @return
	 */
	String desc();

	/**
	 * 是否可空
	 * 
	 * @return
	 */
	boolean canNull() default false;
}
