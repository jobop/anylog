package com.github.jobop.anylog.common.profiler;

import java.io.PrintStream;
import java.util.Calendar;

import com.github.jobop.anylog.common.profiler.core.ExecData;
import com.github.jobop.anylog.common.profiler.core.ExecNode;
import com.github.jobop.anylog.common.profiler.core.LogManager;
import com.github.jobop.anylog.common.profiler.core.LogManagerImpl;
import com.github.jobop.anylog.common.profiler.core.callMonitor.CallMark;
import com.github.jobop.anylog.common.profiler.core.callMonitor.CallMarkManager;
import com.github.jobop.anylog.common.profiler.core.callMonitor.CallMarkManagerImpl;

public class Profiler {
	private final static ThreadLocal<ExecData> dataHolder = new ThreadLocal<ExecData>();

	private static int ELAPSE_TIME_MS_TO_LOG = 500;

	private static LogManager logManager = new LogManagerImpl(System.out);
	private static CallMarkManager callMarkManager = new CallMarkManagerImpl(System.out);

	public static void start(String logName) {
		start(logName, ELAPSE_TIME_MS_TO_LOG);
	}

	public static void init(PrintStream out) {
		logManager = new LogManagerImpl(out);
		callMarkManager = new CallMarkManagerImpl(out);
	}

	public static void start(String logName, int elapseTimeMsToLog) {
		ExecData execData = dataHolder.get();
		ExecNode currentNode = new ExecNode(logName, System.currentTimeMillis(), elapseTimeMsToLog);
		if (execData == null) {
			execData = new ExecData();
			execData.root = currentNode;
			dataHolder.set(execData);
		} else {
			ExecNode parent = execData.currentNode;
			currentNode.setParent(parent);
			parent.getChildList().add(currentNode);
		}
		execData.currentNode = currentNode;
		execData.level++;
	}

	public static void stop(String matchLogNameWithStart) {
		ExecData execData = dataHolder.get();
		long nowTime = System.currentTimeMillis();
		execData.currentNode.setEndTime(nowTime);
		ExecNode child = execData.currentNode;
		execData.currentNode = child.getParent();
		execData.level--;
		if (execData.level == 0) {
			if ((nowTime - execData.root.getStartTime()) >= execData.root.getElapseTimeMsToLog() || execData.showflag) {
				logManager.showTree(execData.root);
			}
			dataHolder.remove();
		} else {
			if (child.getExecTime() >= child.getElapseTimeMsToLog()) {
				execData.showflag = true;
			}
		}
	}

	/**
	 * 记录一个被调用
	 * 
	 * @param methodFullName
	 */
	public static void callMark(String methodFullName) {
		// 产生一个调用信息
		CallMark callMark = new CallMark(Calendar.getInstance(), methodFullName);
		// 管理调用信息
		callMarkManager.showCallMark(callMark);
	}
}
