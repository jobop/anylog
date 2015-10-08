package com.github.jobop.anylog.spi;

import java.io.Serializable;

/**
 * 类型注入的完整描述
 * 
 * @author nevynzheng
 * 
 */
public interface TransformDescriptor extends Serializable {
	public String getNeedInjectClassName();

}
