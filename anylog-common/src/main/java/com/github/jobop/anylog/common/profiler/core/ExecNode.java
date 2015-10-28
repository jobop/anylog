package com.github.jobop.anylog.common.profiler.core;

import java.util.ArrayList;
import java.util.List;

public class ExecNode {
	private String methodName;
	private long startTime;
	private long endTime;
	private long execTime;
	private ExecNode parent;
	private List<ExecNode> childList = new ArrayList<ExecNode>();
	private long elapseTimeMsToLog; 

	public ExecNode(String methodName, long startTime, long elapseTimeMsToLog){
		this.methodName = methodName;
		this.startTime = startTime;
		this.elapseTimeMsToLog = elapseTimeMsToLog;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
		this.execTime = endTime - startTime;
	}

	public List<ExecNode> getChildList() {
		return childList;
	}

	public void setChildList(List<ExecNode> childList) {
		this.childList = childList;
	}

	public ExecNode getParent() {
		return parent;
	}

	public void setParent(ExecNode parent) {
		this.parent = parent;
	}

	public long getExecTime() {
		return execTime;
	}

	public long getElapseTimeMsToLog() {
		return elapseTimeMsToLog;
	}

	public void setElapseTimeMsToLog(long elapseTimeMsToLog) {
		this.elapseTimeMsToLog = elapseTimeMsToLog;
	}

}
