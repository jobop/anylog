package com.github.jobop.anylog.core.interactive.user.servlet;

public class FieldWrapper {
	private String fieldName;
	private String fieldDesc;
	private boolean canNull;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	public boolean isCanNull() {
		return canNull;
	}

	public void setCanNull(boolean canNull) {
		this.canNull = canNull;
	}

}
