package com.github.jobop.anylog.core.exception;

public class NoTransformHandlerFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMsg;

	public NoTransformHandlerFoundException(String errorMsg) {
		this("", errorMsg);
	}

	public NoTransformHandlerFoundException(String errorCode, String errorMsg) {
		super(errorCode + ":" + errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
