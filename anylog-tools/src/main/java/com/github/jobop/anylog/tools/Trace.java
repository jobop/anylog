package com.github.jobop.anylog.tools;

import java.io.File;
import java.io.PrintStream;

public class Trace {
	// 输出流定义为socket??
	public static PrintStream out = System.out;
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				try {
					if (out != null) {
						out.close();
					}
				} catch (Exception e) {
				}
			}
		});
	}

	public static void init(String logsFile) {
		try {
			File f = new File(logsFile);
			if (!f.exists()) {
				f.createNewFile();
			}
			out = new PrintStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> void trace(T t) {
		out.println(t);
	}
}
