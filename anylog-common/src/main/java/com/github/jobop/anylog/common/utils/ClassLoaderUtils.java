package com.github.jobop.anylog.common.utils;

import java.util.HashSet;
import java.util.Set;

public class ClassLoaderUtils {
	public static Set<ClassLoader> findAllClassLoadersByClasses(Class<?>[] classes) {
		Set<ClassLoader> classloaders = new HashSet<ClassLoader>();
		if (null == classes || classes.length == 0) {
			return classloaders;
		}

		for (Class<?> clazz : classes) {
			classloaders.add(clazz.getClassLoader());
		}
		return classloaders;
	}

	public static Set<Class<? extends ClassLoader>> findAllClassLoaderClassesByClasses(Class<?>[] classes) {
		Set<Class<? extends ClassLoader>> classloaders = new HashSet<Class<? extends ClassLoader>>();
		if (null == classes || classes.length == 0) {
			return classloaders;
		}

		for (Class<?> clazz : classes) {
			ClassLoader cl = clazz.getClassLoader();
			if (null != cl) {
				Class<? extends ClassLoader> classloaderClazz = cl.getClass();
				classloaders.add(classloaderClazz);
			}
		}
		return classloaders;
	}
}
