package com.github.jobop.anylog.core.interactive.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandRet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int retCode = 1;
	private String retMsg;
	private List<Throwable> throwables = new ArrayList<Throwable>();

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public List<Throwable> getThrowables() {
		return throwables;
	}

	public void setThrowables(List<Throwable> throwables) {
		this.throwables = throwables;
	}

}
