package com.github.jobop.anylog.common.profiler.core;

import java.util.ArrayList;
import java.util.List;

public class ContextNode {
	private String methodName;
	private long startTime;
	private long endTime;
	private long execTime;
	private ContextNode parent;
	private List<ContextNode> childList = new ArrayList<ContextNode>();
	private long elapseTimeMsToLog; 

	public ContextNode(String methodName, long startTime, long elapseTimeMsToLog){
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

	public List<ContextNode> getChildList() {
		return childList;
	}

	public void setChildList(List<ContextNode> childList) {
		this.childList = childList;
	}

	public ContextNode getParent() {
		return parent;
	}

	public void setParent(ContextNode parent) {
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
