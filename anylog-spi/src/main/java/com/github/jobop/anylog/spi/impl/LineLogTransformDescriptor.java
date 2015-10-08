package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;

public class LineLogTransformDescriptor implements TransformDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String needInjectClassName;
	private String methodName;
	private String lineNum;
	private String lineCode;
	
	

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

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

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

}
