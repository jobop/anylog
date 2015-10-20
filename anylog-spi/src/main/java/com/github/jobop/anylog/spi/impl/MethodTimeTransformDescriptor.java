package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.annotations.SpiDesc;

@SpiDesc(desc = "此功能用于获取某类的某方法的执行时间")
public class MethodTimeTransformDescriptor implements TransformDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpiDesc(desc = "方法所在的类")
	private String needInjectClassName;
	@SpiDesc(desc = "要记录时间的方法")
	private String methodName;

	@Override
	public String getNeedInjectClassName() {
		return needInjectClassName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

}
