package com.github.jobop.anylog.core.interactive.user.servlet;

import java.util.ArrayList;
import java.util.List;

public class ClassWrapper {
	private String className;
	private String classDesc;
	private List<FieldWrapper> fieldWrappers = new ArrayList<FieldWrapper>();

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public List<FieldWrapper> getFieldWrappers() {
		return fieldWrappers;
	}

	public void setFieldWrappers(List<FieldWrapper> fieldWrappers) {
		this.fieldWrappers = fieldWrappers;
	}

}
