package com.github.jobop.anylog.connector;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Map;
import java.util.jar.JarFile;

import com.github.jobop.anylog.common.constans.Constans;
import com.github.jobop.anylog.common.profiler.Profiler;
import com.github.jobop.anylog.core.interactive.system.VMSocketServer;
import com.github.jobop.anylog.core.utils.ArgusUtils;
import com.github.jobop.anylog.tools.Trace;

public class AnyLogAgent {

	// 全局代理方法
	public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException, ClassNotFoundException, IOException {
		try {
			Map<String, String> argusMap = ArgusUtils.args2map(args);
			// here need to add systemclassloader ,or the javassist can not
			// compiler,the javassist's bug?
			// resin容器也需要加到这里才可以，tomcat不用
			initSystemClassPath(inst, argusMap);
			initTools(inst, argusMap);
			startupSocketServer(inst, argusMap);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void startupSocketServer(Instrumentation inst, Map<String, String> argusMap) {
		int port = Integer.valueOf(argusMap.get(ArgusUtils.PORT));
		VMSocketServer server = new VMSocketServer(port, inst);
		server.start();
	}

	private static void initTools(Instrumentation inst, Map<String, String> argusMap) {
		String vmLogFilePath = argusMap.get(Constans.TRACE_LOG_PATH) + Constans.SEPARATOR + argusMap.get(Constans.THIS_PID) + ".log";
		Trace.init(vmLogFilePath);
		Profiler.init(Trace.out);
	}

	private static void initSystemClassPath(Instrumentation inst, Map<String, String> argusMap) {
		String bootsharps = argusMap.get(ArgusUtils.SYSTEM_JARS);
		System.out.println("####bootsharps=" + bootsharps);
		if (null != bootsharps && !bootsharps.equals("")) {
			String[] jarFiles = bootsharps.split("&");
			if (null != jarFiles && jarFiles.length > 0) {
				for (String jarFile : jarFiles) {
					try {
						if (jarFile.contains(Constans.JAR_SUBFIX)) {
							System.out.println("adding jarFile to bootsharp:" + jarFile);
							inst.appendToSystemClassLoaderSearch(new JarFile(jarFile));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
