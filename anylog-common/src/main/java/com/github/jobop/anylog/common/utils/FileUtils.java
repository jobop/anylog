package com.github.jobop.anylog.common.utils;

import java.io.File;

import com.github.jobop.anylog.common.constans.Constans;

public class FileUtils {
	public static String findProviderJarFiles(String dir) {
		StringBuilder sb = new StringBuilder();
		File providersFile = new File(dir);
		if (providersFile.exists() && providersFile.isDirectory()) {
			File[] files = providersFile.listFiles();
			if (null != files && files.length > 0) {
				for (File file : files) {
					if (file.isFile() && file.getName().contains(Constans.JAR_SUBFIX)) {
						sb.append(file.getAbsolutePath()).append("&");
					}
				}
			}
		}
		return sb.toString();
	}
}
