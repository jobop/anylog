package com.github.jobop.anylog.common.profiler.core;

public class ExecData {
	public int level = 0;
	public ExecNode root = null;
	public ExecNode currentNode = null;
	public int elapseTimeMsToLog;
	public boolean showflag;
	
}
