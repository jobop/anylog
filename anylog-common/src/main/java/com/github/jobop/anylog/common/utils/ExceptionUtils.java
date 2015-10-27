package com.github.jobop.anylog.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {
	private static ThreadLocal<List<Throwable>> tl = new ThreadLocal<List<Throwable>>();

	public static void enable() {
		tl.set(new ArrayList<Throwable>());
	}

	public static void addThrowable(Throwable e) {
		if (tl.get() == null) {
			return;
		}
		tl.get().add(e);
	}

	public static void disable() {
		tl.remove();
	}

	public static boolean hasExceptions() {
		if (tl.get() == null) {
			return false;
		}
		if (tl.get().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String dumpMsg() {
		StringBuilder sb = new StringBuilder();
		for (Throwable t : tl.get()) {
			sb.append(t.getMessage()).append(";");
		}
		return sb.toString();
	}
}
