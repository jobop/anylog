package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.annotations.SpiDesc;

@SpiDesc(desc = "此功能用于在某类的某方法的某行插入日志")
public class LineLogTransformDescriptor implements TransformDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpiDesc(desc = "要插入日志的类")
	private String needInjectClassName;
	@SpiDesc(desc = "要插入日志的方法")
	private String methodName;
	@SpiDesc(desc = "要插入日志的所在方法的行数(类中的绝对行数)，代码将会在此行开头加入")
	private String lineNum;
	@SpiDesc(desc = "日志代码,例如$trace(\"12345678\");")
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
