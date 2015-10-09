package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.annotations.SpiDesc;

@SpiDesc(desc = "输出某类异常堆栈，无论异常是否被吃掉")
public class TraceExceptionTransformDescriptor implements TransformDescriptor {
	private static final long serialVersionUID = 1L;
	@SpiDesc(desc = "需要输出的异常类型（例如 java.lang.RuntimeException）")
	private String needInjectClassName;

	@Override
	public String getNeedInjectClassName() {
		return needInjectClassName;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

}
