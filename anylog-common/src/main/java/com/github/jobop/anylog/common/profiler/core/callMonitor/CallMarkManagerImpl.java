package com.github.jobop.anylog.common.profiler.core.callMonitor;

import java.io.PrintStream;

/**
 * 访问信息转化为日志打印出来
 * 
 * @author fx
 * 
 */
public class CallMarkManagerImpl implements CallMarkManager {

	private PrintStream out = System.out;

	public CallMarkManagerImpl(PrintStream out) {
		this.out = out;
	}

	@Override
	public void showCallMark(CallMark callMark) {
		out.println(callMark.toString());
	}

}
