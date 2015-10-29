package com.github.jobop.anylog.common.profiler;

import java.io.PrintStream;

import com.github.jobop.anylog.common.profiler.core.ContextData;
import com.github.jobop.anylog.common.profiler.core.ContextNode;
import com.github.jobop.anylog.common.profiler.core.TraceManager;
import com.github.jobop.anylog.common.profiler.core.TraceManagerImpl;

public class Profiler {
	private final static ThreadLocal<ContextData> dataHolder = new ThreadLocal<ContextData>();

	private static int ELAPSE_TIME_MS_TO_LOG = 500;

	private static TraceManager traceManager = new TraceManagerImpl(System.out);

	public static void start(String logName) {
		start(logName, ELAPSE_TIME_MS_TO_LOG);
	}

	public static void init(PrintStream out) {
		traceManager = new TraceManagerImpl(out);
	}

	public static void start(String logName, int elapseTimeMsToLog) {
		ContextData execData = dataHolder.get();
		ContextNode currentNode = new ContextNode(logName, System.currentTimeMillis(), elapseTimeMsToLog);
		if (execData == null) {
			execData = new ContextData();
			execData.root = currentNode;
			dataHolder.set(execData);
		} else {
			ContextNode parent = execData.currentNode;
			currentNode.setParent(parent);
			parent.getChildList().add(currentNode);
		}
		execData.currentNode = currentNode;
		execData.level++;
	}

	public static void stop(String matchLogNameWithStart) {
		ContextData execData = dataHolder.get();
		long nowTime = System.currentTimeMillis();
		execData.currentNode.setEndTime(nowTime);
		ContextNode child = execData.currentNode;
		execData.currentNode = child.getParent();
		execData.level--;
		if (execData.level == 0) {
			if ((nowTime - execData.root.getStartTime()) >= execData.root.getElapseTimeMsToLog() || execData.showflag) {
				traceManager.showTree(execData.root);
			}
			dataHolder.remove();
		} else {
			if (child.getExecTime() >= child.getElapseTimeMsToLog()) {
				execData.showflag = true;
			}
		}
	}

}
