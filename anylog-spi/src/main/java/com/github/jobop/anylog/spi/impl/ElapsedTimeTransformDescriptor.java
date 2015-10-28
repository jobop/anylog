package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.annotations.SpiDesc;

@SpiDesc(desc = "统计方法耗時，支持方法嵌套后的栈试打印")
public class ElapsedTimeTransformDescriptor implements TransformDescriptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SpiDesc(desc = "要统计耗时的类")
	private String needInjectClassName;
	@SpiDesc(desc = "要统计耗时的方法")
	private String methodName;
	@SpiDesc(desc = "耗时超过多少才输出，毫秒为单位，不填则默认为0，即全部输出。如果嵌套的多个方法设置了不同的值，会使用最外层方法的设值。")
	private String elaspeTime = "0";

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

	public String getElaspeTime() {
		return elaspeTime;
	}

	public void setElaspeTime(String elaspeTime) {
		this.elaspeTime = elaspeTime;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

}
