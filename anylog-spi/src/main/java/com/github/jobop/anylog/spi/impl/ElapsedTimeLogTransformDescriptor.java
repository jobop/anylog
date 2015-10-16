package com.github.jobop.anylog.spi.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.jobop.anylog.anntation.Description;
import com.github.jobop.anylog.spi.TransformDescriptor;

public class ElapsedTimeLogTransformDescriptor implements TransformDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String needInjectClassName;
	private String methodName;
	@Description("example:1,2,3")
	private String lineNumStrWillSplitByComma;

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

	public String getLineNumStrWillSplitByComma() {
		return lineNumStrWillSplitByComma;
	}

	public void setLineNumStrWillSplitByComma(String lineNumStrWillSplitByComma) {
		this.lineNumStrWillSplitByComma = lineNumStrWillSplitByComma;
	}

	public void setNeedInjectClassName(String needInjectClassName) {
		this.needInjectClassName = needInjectClassName;
	}

	public List<Integer> splitLineNum() {
		List<Integer> list = new ArrayList<Integer>();
		List<String> listStr = Arrays.asList(lineNumStrWillSplitByComma.split(","));
		for (String string : listStr) {
			list.add(Integer.valueOf(string));
		}
		return list;
	}
}
