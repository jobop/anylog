package com.github.jobop.anylog.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {
	private static ThreadLocal<List<Throwable>> tl = new ThreadLocal<List<Throwable>>() {
		protected java.util.List<Throwable> initialValue() {
			return new ArrayList<Throwable>();
		};
	};

	public static void addThrowable(Throwable e) {
		tl.get().add(e);
	}

	public static void clear() {
		tl.remove();
	}

	public static String dumpMsg() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Throwable t : tl.get()) {
				sb.append(t.getMessage()).append(";");
			}
		} finally {
			clear();
		}
		System.out.println("####dumpMsg" + sb.toString());
		return sb.toString();
	}
}
