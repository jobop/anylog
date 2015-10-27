package com.github.jobop.anylog.core.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

import com.github.jobop.anylog.common.javassist.ClassPool;
import com.github.jobop.anylog.common.javassist.CtClass;
import com.github.jobop.anylog.common.javassist.NotFoundException;
import com.github.jobop.anylog.common.utils.JavassistUtils;

public class RestoreClassTransformer implements ClassFileTransformer {
	private Set<String> transformedClassSet;

	public RestoreClassTransformer(Set<String> transformedClassSet) {
		super();
		this.transformedClassSet = transformedClassSet;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (!transformedClassSet.contains(className.replace("/", "."))) {
			return null; 
		}

		System.out.println("###restoring class " + className.replace("/", "."));
		try {
			JavassistUtils.addJavassistClassPath(classBeingRedefined);
			CtClass cc = ClassPool.getDefault().get(className.replace("/", "."));
			cc.defrost();
			cc.detach();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} finally {
			JavassistUtils.cleanJavassistClassPath();
		}
		return classfileBuffer;
	}

}
