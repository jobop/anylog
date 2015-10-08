package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;

public class TraceExceptionTransformDescriptor implements TransformDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String needInjectClassName;

	@Override
	public String getNeedInjectClassName() {
		return needInjectClassName;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

}
