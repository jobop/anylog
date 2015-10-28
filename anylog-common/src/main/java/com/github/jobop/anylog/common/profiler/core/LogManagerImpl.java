package com.github.jobop.anylog.common.profiler.core;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogManagerImpl implements LogManager {
	private PrintStream out = System.out;

	public LogManagerImpl(PrintStream out) {
		this.out = out;
	}

	public String showTree(ExecNode root) {
		Locale locale = Locale.CHINA;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
		StringBuilder sb = new StringBuilder(sdf.format(new Date(root.getStartTime())) + "\n");
		long subTime = 0;
		for (ExecNode child : root.getChildList()) {
			subTime += child.getExecTime();
		}
		// 第一次打印栈信息时，在打印日志前加*,以更grep查找
		sb.append("*");
		buildLog(sb, root.getExecTime(), root.getExecTime() - subTime, 100, 0, root.getMethodName());
		for (ExecNode child : root.getChildList()) {
			showNode(sb, 0, child, root);
		}
		String treeLog = sb.toString();
		out.println(treeLog);
		sb = null;
		sdf = null;
		locale = null;
		return treeLog;
	}

	private void showNode(StringBuilder sb, int level, ExecNode node, ExecNode root) {
		level++;
		long subTime = 0;
		for (ExecNode child : node.getChildList()) {
			subTime += child.getExecTime();
		}
		sb.append("+");
		for (int i = 0; i < level; i++) {
			sb.append("---");
		}
		buildLog(sb, node.getExecTime(), node.getExecTime() - subTime, node.getExecTime() * 100 / (node.getParent().getExecTime() == 0 ? 100 : node.getParent().getExecTime()), node.getStartTime() - root.getStartTime(), node.getMethodName());

		for (ExecNode child : node.getChildList()) {
			showNode(sb, level, child, root);
		}
	}

	private void buildLog(StringBuilder sb, long totalTime, long internalTime, long ratio, long startTimeFromRoot, String methodName) {
		sb.append("[" + totalTime + ", " + internalTime + ", " + ratio + "%, " + startTimeFromRoot + "]" + methodName + "\n");// TODO
																																// "要验证回车在win/linux下效果"
	}
}
