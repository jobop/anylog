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
	@SpiDesc(desc = "记录方法入参(y/n)")
	private String logParamsValue = "y";
	@SpiDesc(desc = "记录方法返回值(y/n)")
	private String logReturnValue = "y";

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

	public String getLogParamsValue() {
		return logParamsValue;
	}

	public void setLogParamsValue(String logParamsValue) {
		this.logParamsValue = logParamsValue;
	}

	public String getLogReturnValue() {
		return logReturnValue;
	}

	public void setLogReturnValue(String logReturnValue) {
		this.logReturnValue = logReturnValue;
	}

}
